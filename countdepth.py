
def countdepth(inputstr):
    maxdepth = 0
    itedepth = 0
    depth = 0
    depthstack = []
    n = 0
    while n < len(inputstr):
        i = inputstr[n]
        if i == "(":
            if inputstr[n:].startswith("(ite"):
                itedepth += 1
                if itedepth > maxdepth:
                    maxdepth = itedepth
                depthstack.append(depth)
                depth = 0
            else:
                depth += 1
        if i == ")":
            if depth == 0:
                itedepth -= 1
                depth = depthstack.pop()
            else:
                depth -= 1
        n += 1
    return maxdepth

if __name__ == "__main__":
    import sys
    print(countdepth(sys.stdin.read()))




