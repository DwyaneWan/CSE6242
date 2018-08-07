lines_seen = set() # holds lines already seen
outfile = open("graph2.csv", "w")
for line in open("graph.csv", "r"):
    if line not in lines_seen: # not a duplicate
        outfile.write(line)
        lines_seen.add(line)
outfile.close()