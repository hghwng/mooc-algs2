#!/usr/bin/env python3


class HuffmanNode:
    def __init__(self):
        self.f = -1
        self.c = None
        self.l = self.r = None

    def __lt__(self, other):
        return self.f < other.f

    def get(self, q):
        if self.c == q:
            return 0
        if self.l:
            d = self.l.get(q)
            if d >= 0:
                return d + 1
        if self.r:
            d = self.r.get(q)
            if d >= 0:
                return d + 1
        return -1


def make_bytes(text):
    ret = []
    for i in text.strip().split():
        ret.append(int(i, base=16))
    return ret


def solve_q1(text):
    from queue import PriorityQueue
    freq = dict()
    for i in text:
        if i not in freq:
            freq[i] = 0
        freq[i] += 1

    q = PriorityQueue()
    for i in freq:
        n = HuffmanNode()
        n.f = freq[i]
        n.c = i
        q.put(n)

    while q.qsize() > 1:
        a = q.get()
        b = q.get()
        c = HuffmanNode()
        c.f = a.f + b.f
        c.l = a
        c.r = b
        q.put(c)

    root = q.get()
    ret = 0
    for i in freq:
        ret += freq[i] * root.get(i)
    print(ret)


def solve_q2(byte):
    MID = 0x80
    table = list()
    output = ''

    for i in range(0, MID):
        table.append(chr(i))

    table.append(None)      # EOF

    pre = chr(byte[0])
    output += pre
    for i in range(1, len(byte)):
        b = byte[i]
        if b == MID:
            break

        if b == len(table):
            dec = pre + pre[0]
        else:
            dec = table[b]

        table.append(pre + dec[0])
        output += dec
        pre = dec
    for i in output:
        print(i, end=' ')
    print()


class LzwNode:
    def __init__(self):
        self.arr = dict()
        self.val = -1

    def get(self, text):
        if len(text) == 0:
            return (self.val, 0)

        if text[0] not in self.arr:
            return (self.val, 0)

        r = self.arr[text[0]].get(text[1:])
        return (r[0], r[1] + 1)

    def put(self, text, v):
        if len(text) == 0:
            self.val = v
            return

        if text[0] not in self.arr:
            self.arr[text[0]] = LzwNode()
        self.arr[text[0]].put(text[1:], v)


def solve_q3(text):
    root = LzwNode()
    output = list()

    MID = 0x80
    table_idx = MID
    i = 0
    while i < len(text):
        val, depth = root.get(text[i:])
        if val == -1:
            output.append(ord(text[i]))
            depth = 1
        else:
            output.append(val)
        if i + depth < len(text):
            table_idx += 1
            peek = text[i:i + depth + 1]
            root.put(peek, table_idx)
        i += depth

    output.append(MID)
    for i in output:
        print(hex(i)[2:], end=' ')
    print()


q1 = 'WYYYYKYPKDYWYYPAWDKKUYDUWYDWWDYUUDWDWDWDWKYPDW'
q2 = '    41 41 43 42 83 84 41 86 82 43 80'
q3 = '     B C C C B B B A B A C C B A C'

solve_q1(q1.strip())
solve_q2(make_bytes(q2))
solve_q3(q3.strip().replace(' ', ''))
