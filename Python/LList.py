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

class Node(object):
    def __init__(self, datum):
        self.datum = datum
        self.next = None
    def __str__(self):
        return repr(self.datum)
    def getDatum(self):
        return self.datum
    def getNext(self):
        return self.next
    def setNext(self, next):
        self.next = next

class LList(object):
    head = None
    tail = None
    def __init__(self):
        pass
    def __str__(self):
        return " -> ".join(map(lambda node: str(node), self))
    def __add__(self, datum):
        node = Node(datum)
        if self.head == None:
            self.head = node
        if self.tail != None:
            self.tail.setNext(node)
        self.tail = node
        return self
    def __sub__(self, node):
        prev = filter(lambda test: (test.getNext() == node), self)
        if prev: 
            prev[0].setNext(node.getNext())
        return self
    def __iter__(self):
        self.iternode = None
        self.iterfast = self.head
        return self
    def next(self):
        if self.head == None:
            raise StopIteration
        if self.iterfast: self.iterfast = self.iterfast.getNext()
        if self.iterfast: self.iterfast = self.iterfast.getNext()
        if self.iterfast and self.iterfast == self.iternode:
            raise CircularDependency("Multiple nodes point to: " + str(self.iterfast))
        if self.iternode == None:
            self.iternode = self.head
            return self.iternode
        if self.iternode.getNext() == None:
            raise StopIteration
        else:
            self.iternode = self.iternode.getNext()
            return self.iternode
    def insert(self, datum, next = None):
        prev = None
        if next == self.head:
            node = Node(datum)
            node.setNext(self.head)
            self.head = node
            return self
        elif next:
            try:
                prev = filter(lambda test: (test.getNext() == next), self)[0]
            except IndexError:
                raise NodeNotFound(next)
        else:
            prev = self.tail
        node = Node(datum)
        node.setNext(prev.getNext())
        prev.setNext(node)
        if self.tail == prev:
            self.tail = node
        return self
    def reverse(self):
        if not self.head.getNext():
            return self
        prev  = None
        pprev = None
        if self.head.getNext() and self.head.getNext().getNext():
            for node in self:
                if not prev:
                    prev = node
                    continue
                if not pprev:
                    pprev = prev
                    prev  = node
                    continue
                prev.setNext(pprev)
                pprev, prev = prev, node
        elif self.head.getNext():
            prev  = self.tail
            pprev = self.head
        prev.setNext(pprev)
        self.head, self.tail = self.tail, self.head
        self.tail.setNext(None)
        return self
    def getNodes(self, datum):
        return filter(lambda node: (node.getDatum() == datum), self)
