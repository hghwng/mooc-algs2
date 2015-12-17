#!/usr/bin/env python3


def make_arr(text, var_size):
    import re
    regex = re.compile('([+ -] *\d+(/\d+)?)( x\d+)?')
    arr = list()
    for line in text.splitlines():
        pairs = regex.findall(line)
        if len(pairs) == 0:
            continue

        arr.append([0] * (var_size + 1))
        for i in pairs:
            num = eval(i[0].replace(' ', ''))
            var = i[2].strip()
            if var == '':
                arr[-1][-1] = num
            else:
                arr[-1][int(var[1:])] = num
    return arr


def solve_q1(arr):
    cols = len(arr[0])
    for c in range(0, cols - 1):
        if arr[0][c] > 0:
            print('x' + str(c), end=' ')
    print()


def solve_q2(arr, col):
    cols = len(arr[0])
    rows = len(arr)
    EPS = 1e-9
    min_list = []
    for r in range(1, rows):
        ratio = arr[r][-1] / arr[r][col]
        if ratio < 0:
            continue
        if len(min_list) == 0:
            min_list.append(r)
            continue

        min_ratio = arr[min_list[0]][-1] / arr[min_list[0]][col]
        if abs(ratio - min_ratio) < EPS:
            min_list.append(r)
        elif ratio < min_ratio:
            min_ratio = ratio
            min_list = [r]

    for row in min_list:
        for col in range(cols):
            if abs(arr[row][col] - 1) > EPS:
                continue
            for r in range(1, rows):
                ok = True
                if r != row and arr[r][col] != 0:
                    ok = False
                    break
            if ok:
                print('x' + str(col), end=' ')
    print()


q1 = '''
     +  5/4 x0  -  5/3 x1  +  8/5 x2                                   -    5 x6  -  9/2 x7    -  Z    = -120
    ---------------------------------------------------------------------------------------------------------
     +  3/4 x0  -  3/5 x1  +    3 x2                        +    1 x5  -  1/2 x6  -  7/2 x7            =   42
     -  1/5 x0  -  7/5 x1  +    3 x2  +    1 x3                        +  5/4 x6  +  2/3 x7            =    6
     +  7/4 x0  +    4 x1  +  1/2 x2             +    1 x4             -  3/4 x6  -    1 x7            =   48
'''

q2 = '''
     -  3/2 x0                        +    2 x3  -  8/3 x4             -    1 x6                          -  Z    = -324
    --------------------------------------------------------------------------------------------------------------------
     -  9/4 x0                        -    3 x3  +  5/2 x4             +  1/5 x6             +    1 x8            =   24
     +  1/5 x0                        +    1 x3  -    2 x4  +    1 x5  -  5/3 x6                                  =   18
     +    1 x0             +    1 x2  +  5/4 x3  +    1 x4             +  9/5 x6                                  =   42
     -  2/3 x0                        +    2 x3  +    2 x4             -    9 x6  +    1 x7                       =   36
     -  2/3 x0  +    1 x1             +  8/3 x3  -  1/2 x4             -  5/2 x6                                  =   48
'''
q2_chosen = 'x3'

solve_q1(make_arr(q1, 8))
solve_q2(make_arr(q2, 9), int(q2_chosen.strip()[1:]))
