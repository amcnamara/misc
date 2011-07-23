## Error objects
class CircularDependency(Exception):
    def __init__(self, msg = ""):
        self.msg = msg
    def __str__(self):
        return repr(self.msg)

class NodeNotFound(Exception):
    def __init__(self, msg = ""):
        self.msg = msg
    def __str__(self):
        return repr(self.msg)

## Composite objects of the LList structure
class Node(object):
    '''Node container for LList implementation'''
    datum = None
    next  = None
    def __init__(self, datum):
        self.datum = datum
        self.next = None
    def __str__(self):
        return repr(self.datum)

class Endpoint(object):
    '''Storing head/tail pointers in objects ensure that LList is copy-safe, otherwise its head/tail pointers would be corrupted on shallow copy with in-place operations.'''
    node = None
    def __init__(self, point = None):
        self.node = point
    def __eq__(self, comp):
        return comp == self.node

## Linked List implementation
class LList(object):
    '''Linked List implementation, operations are in-place, structure is shallow-copy safe.'''
    def __init__(self):
        self.head = Endpoint()
        self.tail = Endpoint()
    def __str__(self):
        return " -> ".join(map(lambda node: str(node), self))
    def __add__(self, datum):
        node = Node(datum)
        if self.head.node == None:
            self.head.node = node
        if self.tail.node != None:
            self.tail.node.next = node
        self.tail.node = node
        return self
    def __sub__(self, node):
        if self.head.node == node:
            self.head.node = self.head.node.next
            if self.tail.node == node:
                self.tail.node = None
        prev = filter(lambda test: test.next == node, self)
        if prev: 
            prev[0].next = node.next
        return self
    def __iter__(self):
        self.iternode = None
        self.iterfast = self.head.node
        return self
    def next(self):
        if self.head == None:
            raise StopIteration
        ## On each iteration move the fast iterator ahead twice, ahead of the normal iterator
        if self.iterfast: self.iterfast = self.iterfast.next
        if self.iterfast: self.iterfast = self.iterfast.next
        ## If the slow iterator ever catches the fast, there is a cycle
        if self.iterfast and self.iterfast == self.iternode:
            raise CircularDependency
        if self.iternode == None:
            self.iternode = self.head.node
            return self.iternode
        if self.iternode.next == None:
            raise StopIteration
        else:
            self.iternode = self.iternode.next
            return self.iternode
    def insert(self, datum, next = None):
        node = Node(datum)
        prev = None
        if not next or not self.head or next == self.head.node:
            next = self.head.node
            self.head.node = node
        else:
            try:
                prev = filter(lambda test: test.next == next, self)[0]
            except IndexError:
                raise NodeNotFound(next)
            prev.next = node
        node.next = next
        return self
    def reverse(self):
        ## Case: 0-1 elements in list
        if not self.head or not self.head.node or not self.head.node.next:
            return self
        prev  = None
        pprev = None
        ## Case: >2 elements in list
        ## Store the first two nodes, iterate through the nodes and assign pointers back to previously iterated nodes
        if self.head.node.next and self.head.node.next.next:
            for node in self:
                if not prev:
                    prev = node
                    continue
                if not pprev:
                    pprev = prev
                    prev  = node
                    continue
                prev.next = pprev
                pprev, prev = prev, node
        ## Case: 2 elements in list
        else:
            prev  = self.tail.node
            pprev = self.head.node
        ## Clean up the remaining two pointers near the end of the list
        prev.next = pprev
        self.head.node, self.tail.node = self.tail.node, self.head.node
        self.tail.node.next = None
        return self
    def getNodes(self, datum):
        return filter(lambda node: node.datum == datum, self)
