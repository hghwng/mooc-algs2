#!/usr/bin/env python3
class Edge:
    def __init__(self, src, dst, flow, cap):
        self.src = src
        self.dst = dst
        self.cap = cap
        self.flow = flow

    def have_other(self, src):
        if self.src == src:
            return self.dst if self.cap < self.flow else None
        else:  # self.src == dst
            return self.src if self.cap > 0 else None

    def other(self, src):
        return self.src if src == self.dst else self.dst

    def __repr__(self):
        return '{}->{} {} / {}'.format(
                to_str(self.src), to_str(self.dst),
                self.flow, self.cap)


def to_index(x):
    return ord(x) - ord('A')


def to_str(x):
    return chr(ord('A') + x)


def make_graph(text, maxlabel):
    import re
    regex = re.compile('([A-Z])->([A-Z]) +(\d+) +/ +(\d+)')
    graph = list(map(lambda x: list(), range(maxlabel + 1)))
    for group in regex.findall(text):
        src = to_index(group[0])
        dst = to_index(group[1])
        edge = Edge(src, dst, int(group[2]), int(group[3]))
        graph[src].append(edge)
        graph[dst].append(edge)

    return graph


def solve_q1(graph, dst):
    from collections import deque
    q = deque()
    pre = dict()

    src = 0
    q.append(src)
    reached = {src}
    while len(q):
        src = q.pop()
        for i in graph[src]:
            newsrc = None
            if src == i.src:
                if i.dst not in reached and i.flow < i.cap:
                    newsrc = i.dst
            else:  # src == i.dst
                if i.src not in reached and i.flow > 0:
                    newsrc = i.src

            if newsrc:
                pre[newsrc] = src
                reached.add(newsrc)
                q.append(newsrc)
                if newsrc == dst:
                    src = dst
                    answer = ''
                    while src in pre:
                        answer += to_str(src) + ' '
                        src = pre[src]
                    answer += 'A'
                    print(answer[::-1])


def solve_q2(graph):
    from collections import deque
    q = deque()

    src = 0
    q.append(src)
    reached = {src}
    while len(q):
        src = q.pop()
        for i in graph[src]:
            newsrc = None
            if src == i.src:
                if i.dst not in reached and i.flow < i.cap:
                    newsrc = i.dst
            else:  # src == i.dst
                if i.src not in reached and i.flow > 0:
                    newsrc = i.src

            if newsrc:
                reached.add(newsrc)
                q.append(newsrc)

    for i in reached:
        print(to_str(i), end=' ')
    print()


q1 = '''
    A->B     26  /  27
    A->F     17  /  17
    A->G      8  /  14
    B->C     26  /  26
    C->D     19  /  19
    C->I     20  /  20
    D->E      7  /   9
    D->J     24  /  30
    E->J      7  /   7
    F->G     17  /  17
    G->B      0  /   7
    G->C     10  /  17
    G->H     15  /  15
    H->C      3  /  14
    H->I     12  /  15
    I->D     12  /  12
    I->J     20  /  26
'''

q2 = '''
    A->B     20  /  20
    A->G     24  /  29
    A->F      0  /   9
    B->G      1  /   7
    B->H      7  /  11
    B->C     12  /  12
    C->H      0  /  13
    C->D     12  /  13
    D->H      0  /  11
    D->I      3  /   6
    D->E      7  /   7
    D->J      2  /  14
    E->J      7  /  10
    F->G      0  /  14
    G->H     25  /  25
    H->I     32  /  32
    I->J     35  /  35
'''

maxlabel = to_index('J')
solve_q1(make_graph(q1, maxlabel), maxlabel)
solve_q2(make_graph(q2, maxlabel))
