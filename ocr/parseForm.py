## Author:  Alex McNamara
## License: Public domain

import sys
import cv
import operator
from math import sin, cos, tan, sqrt, pow, pi, fabs

def __fuzzyEq (pt1x, pt1y, pt2x, pt2y):
    fuzz = 10
    return ((fabs(pt1x - pt2x) < fuzz) and (fabs(pt1y - pt2y) < fuzz))

def __tan (opp, adj):
    try:
        result = tan((float)(opp) / adj)
    except ZeroDivisionError:
        result = 0
    return result

def __isLonger (((l1pt1x, l1pt1y), (l1pt2x, l1pt2y)), ((l2pt1x, l2pt1y), (l2pt2x, l2pt2y))):
    dy1 = l1pt2y - l1pt1y
    dx1 = l1pt2x - l1pt1x
    dy2 = l2pt2y - l2pt1y
    dx2 = l2pt2x - l2pt1x

    len1 = sqrt(pow(dx1, 2) + pow(dy1, 2))
    len2 = sqrt(pow(dx2, 2) + pow(dy2, 2))

    return (len1 > len2)

def __isOverlapping (((l1pt1x, l1pt1y), (l1pt2x, l1pt2y)), ((l2pt1x, l2pt1y), (l2pt2x, l2pt2y))):
    fuzz = 5

    dy1 = l1pt2y - l1pt1y
    dx1 = l1pt2x - l1pt1x
    dy2 = l2pt2y - l2pt1y
    dx2 = l2pt2x - l2pt1x

    anglex1 = __tan(dy1, dx1)
    angley1 = __tan(dx1, dy1)
    anglex2 = __tan(dy2, dx2)
    angley2 = __tan(dx2, dy2)
    
    return ((not __isLonger(((l1pt1x, l1pt1y), (l1pt2x, l1pt2y)), ((l2pt1x, l2pt1y), (l2pt2x, l2pt2y)))) and
            (__fuzzyEq(dx1, dy1, dx2, dy2)) and
            (fabs(anglex1 - anglex2) < fuzz) and
            (fabs(angley1 - angley1) < fuzz))

def __isConnected (((l1pt1x, l1pt1y), (l1pt2x, l1pt2y)), ((l2pt1x, l2pt1y), (l2pt2x, l2pt2y))):
    if ((l1pt1x == l2pt1x) and
        (l1pt1y == l2pt1y) and
        (l1pt2x == l2pt2x) and
        (l1pt2x == l2pt2x)):
        return False
    else:
        return ((not __isOverlapping(((l1pt1x, l1pt1y), (l1pt2x, l1pt2y)), ((l2pt1x, l2pt1y), (l2pt2x, l2pt2y)))) and
                __fuzzyEq(l1pt1x, l1pt1y, l2pt1x, l2pt1y) or
                __fuzzyEq(l1pt1x, l1pt1y, l2pt2x, l2pt2y) or
                __fuzzyEq(l1pt2x, l1pt2y, l2pt1x, l2pt1y) or
                __fuzzyEq(l1pt2x, l1pt2y, l2pt2x, l2pt2y))

def __flatten (seq):
    return (reduce(list.__add__, (list(flattenthis or None) for flattenthis in seq)))

def __anyTrue (seq):
    return reduce(operator.or_, seq)

def __allTrue (seq):
    return reduce(operator.and_, seq)

def __uniquePolys (buf):
    for poly in buf:
        restbuf = []
        restbuf.extend(buf)
        restbuf.remove(poly)
        
        for testpoly in restbuf:
            ## If all points in this testpoly are fuzzy-common with poly, remove it from the buffer
            if (__allTrue(map((lambda applytest:
                                   (__anyTrue(map((lambda testpoint:
                                                       (applytest(testpoint))),
                                                  testpoly)))),
                              (map((lambda pt1:
                                        (lambda pt2:
                                             (__fuzzyEq(pt1[0], pt1[1], pt2[0], pt2[1])))),
                                   poly))))):
                    buf.remove(testpoly)

def parseImage (img):
    edgemap = cv.CreateImage(cv.GetSize(img), 8, 1)
    overlay = cv.CreateImage(cv.GetSize(img), 8, 3)
    formrects = []
    formlines = []

    ## Build the edgemap and get a seq of lines from it
    cv.Canny(img, edgemap, 0.50, 1.0, 3)
    storage = cv.CreateMemStorage(0)
    
    ## TODO: This is currently tuned semi-well but gets flaky on noisy/busy content, 
    ##       verify each line and remove fuzzyeqs, as well as mid-line intersects.
    lines = cv.HoughLines2(edgemap, storage, cv.CV_HOUGH_PROBABILISTIC, 5, pi / 180, 100, 50, 50)
    formlines.extend(lines)

    for line in lines:
        ## Snag subset of lines that doesn't inclue this one
        restlines = []
        restlines.extend(list(lines))
        restlines.remove(tuple(line))

        ## Create a partial fn to filter lines connected to this one
        d0connectedfn = lambda someline: __isConnected(line, someline)
        d1connections = filter(d0connectedfn, restlines)

        ## Create a seq and applicator of curried fns of 1st connections and filter again
        d1connectedfns = map((lambda l1: 
                              (lambda l2: 
                               __isConnected(l1, l2))), 
                             d1connections)
        applyd1connectedfns = (lambda testline:
                                   (map((lambda nextd1curry:
                                             (nextd1curry(testline)) and testline), ## Ugly syntactic sugar for if d1curry(testline) push testline out
                                        d1connectedfns)))

        ## Remove d1connections, they're unimportant for d2 tests and it cuts down comparisons.
        for d1connection in d1connections:
            restlines.remove(d1connection)
        d2connections = __flatten(map(applyd1connectedfns, restlines))
        d2connections = filter((lambda identity: identity), d2connections) ## This kills False returns on connections

        ## Count dupes to find if any 2nd order connections are common (ie. a loop of 4 sides), make entries unique
        oppositelines = list(set(filter((lambda item: (d2connections.count(item)) > 1), d2connections)))
            
        ## Create a poly bound between this line and any 2nd order connection (opposite side)
        for opp in oppositelines:
            poly = [line[0], line[1], opp[0], opp[1]]
            formrects.append(poly)
            try:
                formlines.remove(opp)
                formlines.remove(line)
            except:
                continue ## Fail silently on remove line (not-there error), since its removed many times.  
                         ## TODO: Find a safer/quieter way to remove from a collection
    __uniquePolys(formrects)

    if sys.flags.debug:
        print "Polys:           " + str(formrects)
        print "Remaining lines: " + str(formlines)
    
    ## Draw detected polys and lines on the overlay image and save
    for poly in formrects:
        cv.FillConvexPoly(overlay, poly, cv.RGB(255,255,0))
    for line in formlines:
        cv.Line(overlay, line[0], line[1], cv.CV_RGB(255, 0, 0), 3, 8)
    cv.SaveImage("overlay.png", overlay)    

## Command-line interface
if __name__ == "__main__":
    if (len(sys.argv) > 1):
        filename = sys.argv[1]
        try:
            img = cv.LoadImage(filename, cv.CV_LOAD_IMAGE_GRAYSCALE)
        except:
            print "Unable to open image file \'" + str(filename) + "\'"
            raise
        parseImage(img)
    else:
        print "Usage: python [-d] parseForm.py <image-filename>"
