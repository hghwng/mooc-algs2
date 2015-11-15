#!/usr/bin/env python3
class UnionSet:
    def __init__(self, n):
        self.arr = []
        for i in range(n):
            self.arr.append(i)

    def query(self, i):
        while self.arr[i] != self.arr[self.arr[i]]:
            self.arr[i] = self.arr[self.arr[i]]
        return self.arr[i]

    def join(self, i, j):
        self.arr[self.query(i)] = self.query(j)

    def same(self, i, j):
        return self.query(i) == self.query(j)


def make_graph(text):
    graph = {}
    edges = []
    for line in text.split('\n'):
        val = -1
        v0 = v1 = None
        for i in range(len(line)):
            char = line[i]
            if char == ' ' or char == '-':
                continue

            if ord('0') <= ord(char) <= ord('9'):
                val = int(line[i:])
                break

            if ord('A') <= ord(char) <= ord('Z'):
                if v0 is None:
                    v0 = ord(char) - ord('A')
                elif v1 is None:
                    v1 = ord(char) - ord('A')

        if val != -1 and v0 is not None and v1 is not None:
            if v0 not in graph:
                graph[v0] = dict()
            if v1 not in graph:
                graph[v1] = dict()
            graph[v0][v1] = graph[v1][v0] = val
            edges.append((val, v0, v1))

    return graph, edges


def kruskal(edges):
    edges.sort(key=lambda x: x[0])
    us = UnionSet(30)
    for edge in edges:
        if us.same(edge[1], edge[2]):
            continue
        us.join(edge[1], edge[2])
        print(edge[0], end=' ')
    print()


def prim(graph, start):
    import queue
    todo = queue.PriorityQueue()
    done = set([start])

    for k, v in graph[start].items():
        todo.put((v, k))

    while not todo.empty():
        elem = todo.get()
        v = elem[0]
        k = elem[1]
        if k in done:
            continue
        done.add(k)
        print(v, end=' ')

        for k, v in graph[k].items():
            if k in done:
                continue
            todo.put((v, k))
    print()

q1 = '''
    A-B    17
    A-F    15
    F-B    11
    G-B     7
    C-B     5
    B-H     4
    C-H     6
    D-C     2
    I-C     1
    E-D    16
    D-J    12
    I-D     8
    J-E    13
    F-G     9
    H-G    14
    H-I     3
    I-J    10'''

q2 = '''
    A-F      16
    A-B      13
    B-G      12
    H-B       9
    F-B       6
    C-B       4
    I-C      15
    C-D       7
    H-C       1
    D-J      17
    E-D      10
    D-I       3
    E-J       8
    F-G       5
    H-G      11
    I-H      14
    J-I       2'''

q2_start = 'F'

kruskal(make_graph(q1)[1])
prim(make_graph(q2)[0], ord(q2_start) - ord('A'))
