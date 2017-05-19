## Outer

``` 
Outer(f, x, y)
```

> computes a generalised outer product of `x` and `y`, using the function `f` in place of multiplication.

``` 
>> Outer(f, {a, b}, {1, 2, 3})
{{f(a, 1), f(a, 2), f(a, 3)}, {f(b, 1), f(b, 2), f(b, 3)}}
``` 

Outer product of two matrices:
``` 
>> Outer(Times, {{a, b}, {c, d}}, {{1, 2}, {3, 4}})
{{{{a, 2 a}, {3 a, 4 a}}, {{b, 2 b}, {3 b, 4 b}}}, {{{c, 2 c}, {3 c, 4 c}}, {{d, 2 d}, {3 d, 4 d}}}}
``` 

Outer of multiple lists:
``` 
>> Outer(f, {a, b}, {x, y, z}, {1, 2})
{{{f(a, x, 1), f(a, x, 2)}, {f(a, y, 1), f(a, y, 2)}, {f(a, z, 1), f(a, z, 2)}}, {{f(b, x, 1), f(b, x, 2)}, {f(b, y, 1), f(b, y, 2)}, {f(b, z, 1), f(b, z, 2)}}}
``` 
 
Arrays can be ragged:
``` 
>> Outer(Times, {{1, 2}}, {{a, b}, {c, d, e}})
{{{{a, b}, {c, d, e}}, {{2 a, 2 b}, {2 c, 2 d, 2 e}}}}
``` 

Word combinations:
``` 
>> Outer(StringJoin, {"", "re", "un"}, {"cover", "draw", "wind"}, {"", "ing", "s"}) 
{{{"cover", "covering", "covers"}, {"draw", "drawing", "draws"}, {"wind", "winding", "winds"}}, {{"recover", "recovering", "recovers"}, {"redraw", "redrawing", "redraws"}, {"rewind", "rewinding", "rewinds"}}, {{"uncover", "uncovering", "uncovers"}, {"undraw", "undrawing", "undraws"}, {"unwind", "unwinding", "unwinds"}}}
``` 

Compositions of trigonometric functions:
``` 
>> trigs = Outer(Composition, {Sin, Cos, Tan}, {ArcSin, ArcCos, ArcTan})
{{Composition(Sin, ArcSin), Composition(Sin, ArcCos), Composition(Sin, ArcTan)}, {Composition(Cos, ArcSin), Composition(Cos, ArcCos), Composition(Cos, ArcTan)}, {Composition(Tan, ArcSin), Composition(Tan, ArcCos), Composition(Tan, ArcTan)}}
``` 

Evaluate at `0`:
``` 
>> Map(#(0) &, trigs, {2})
{{0, 1, 0}, {1, 0, 1}, {0, ComplexInfinity, 0}}
``` 