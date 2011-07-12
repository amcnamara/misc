class Node(object):
    def __init__(self, datum):
        self.datum = datum
        self.next = None
    def getNext(self):
        return self.next
    def setNext(self, next):
        print next
        self.next = next
    def getDatum(self):
        return self.datum

class LList(object):
    head = None
    tail = None
    def __init__(self):
        pass
    def __iter__(self):
        self.iternode = None
        self.iteritems = []
        return self
    def next(self):
        if self.head == None:
            raise StopIteration
        if self.head == self.tail:
            return self.head
        if self.iternode == None:
            self.iternode = self.head
            return self.iternode
        if self.iternode != self.tail:
            if self.iternode in self.iteritems:
                print "circular list, " + self.iternode
                raise StopIteration
            self.iteritems.append(self.iternode)
            self.iternode = self.iternode.getNext()
            return self.iternode
        else:
            raise StopIteration
    def add(self, datum, next = None):
        node = Node(datum)
        if next:
            node.setNext(next)
        if self.tail != None:
            self.tail.setNext(node)
        self.tail = node
        if self.head == None:
            self.head = self.tail

def getmean(lst):
    avg = 0.0
    count = 0
    for elt in lst:
        count += 1
        avg += elt.getDatum()
    avg /= count
    return avg

llist = LList()
llist.add(2)
llist.add(2)
repnode = llist.tail
llist.add(2)
llist.add(2)
llist.add(2, repnode)
llist.add(10)

print(getmean(llist))
