var x = {a : "aa", b:"bb", c:"cc"}
var f = "";
for (t in x)
    f = f + t;
TAJS_dumpValue(f)

if (Math.random())
    var x = 42
var s = ""
for (t in x) 
    s = s + t;
TAJS_dumpValue(s)
