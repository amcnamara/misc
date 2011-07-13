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
    datum = None
    next  = None
    def __init__(self, datum):
        self.datum = datum
        self.next = None
    def __str__(self):
        return repr(self.datum)

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
            self.tail.next = node
        self.tail = node
        return self
    def __sub__(self, node):
        prev = filter(lambda test: test.next == node, self)
        if prev: 
            prev[0].next = node.next
        return self
    def __iter__(self):
        self.iternode = None
        self.iterfast = self.head
        return self
    def next(self):
        if self.head == None:
            raise StopIteration
        if self.iterfast: self.iterfast = self.iterfast.next
        if self.iterfast: self.iterfast = self.iterfast.next
        if self.iterfast and self.iterfast == self.iternode:
            raise CircularDependency
        if self.iternode == None:
            self.iternode = self.head
            return self.iternode
        if self.iternode.next == None:
            raise StopIteration
        else:
            self.iternode = self.iternode.next
            return self.iternode
    def insert(self, datum, next = None):
        node = Node(datum)
        prev = None
        if not next or next == self.head:
            next = self.head
            self.head = node
        else:
            try:
                prev = filter(lambda test: test.next == next, self)[0]
            except IndexError:
                raise NodeNotFound(next)
            prev.next = node
        node.next = next
        return self
    def reverse(self):
        if not self.head.next:
            return self
        prev  = None
        pprev = None
        if self.head.next and self.head.next.next:
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
        elif self.head.next:
            prev  = self.tail
            pprev = self.head
        prev.next = pprev
        self.head, self.tail = self.tail, self.head
        self.tail.next = None
        return self
    def getNodes(self, datum):
        return filter(lambda node: node.datum == datum, self)
