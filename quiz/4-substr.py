#!/usr/bin/env python3


def arr_from_str(text):
    arr = list()
    for i in text:
        if ord('A') <= ord(i) <= ord('Z'):
            arr.append(ord(i) - ord('A'))
    return arr


def solve_q1(pat, radix, target):
    dfa = [[0] * radix]
    dfa[0][pat[0]] = 1

    pre = 0
    for i in range(1, len(pat)):
        v = list()
        for j in range(radix):
            v.append(dfa[pre][j])
        v[pat[i]] = i + 1
        pre = dfa[pre][pat[i]]
        dfa.append(v)

    for i in dfa:
        print(i[target], end=' ')
    print()


def solve_q2(txt, pat):
    fallback = [-1] * 26
    for i in range(len(pat)):
        fallback[pat[i]] = i

    answer = ''
    i = 0
    while i < len(txt) - len(pat):
        f = 0
        answer += chr(ord('A') + txt[i + len(pat) - 1])
        for j in range(len(pat) - 1, -1, -1):
            if pat[j] == txt[i + j]:
                continue
            f = j - fallback[txt[i + j]]
            if f < 1:
                f = 1
            break

        if f == 0:
            print(' '.join(answer))
            return
        i += f


def solve_q3(head, tail, hash, Q, RM):
    print(((hash + Q - head * RM % Q) * 10 + tail) % Q)


q1 = 'C C B C C C B A '
q1_target = 'C'

q2_pat = 'Y F A T H E R '
q2_txt = 'B R O T H E R T H A T F A T H E R W A S M Y F A T H E R T H '

q3_head = 7
q3_tail = 4
q3_hash = 35
q3_Q = 71
q3_RM = 5

solve_q1(arr_from_str(q1), 3, ord(q1_target) - ord('A'))
solve_q2(arr_from_str(q2_txt), arr_from_str(q2_pat))
solve_q3(q3_head, q3_tail, q3_hash, q3_Q, q3_RM)
