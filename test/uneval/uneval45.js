function f(y) {
   a = y
   return eval("z = " + a)
}

TAJS_dumpValue(f("3"))
TAJS_dumpValue(f("5"))
