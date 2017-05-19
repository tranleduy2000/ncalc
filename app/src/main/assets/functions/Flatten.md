## Flatten

```
Flatten(expr)
```

> flattens out nested lists in `expr`.
	
```
Flatten(expr, n)
```

> stops flattening at level `n`.
	
```
Flatten(expr, n, h)
```

> flattens expressions with head `h` instead of 'List'.

### Examples
```
>> Flatten({{a, b}, {c, {d}, e}, {f, {g, h}}})
{a, b, c, d, e, f, g, h}
>> Flatten({{a, b}, {c, {e}, e}, {f, {g, h}}}, 1)
{a, b, c, {e}, e, f, {g, h}}
>> Flatten(f(a, f(b, f(c, d)), e), Infinity, f)
f(a, b, c, d, e)
>> Flatten({{a, b}, {c, d}}, {{2}, {1}})
{{a, c}, {b, d}}
>> Flatten({{a, b}, {c, d}}, {{1, 2}})
{a, b, c, d}
```

Flatten also works in irregularly shaped arrays
```
>> Flatten({{1, 2, 3}, {4}, {6, 7}, {8, 9, 10}}, {{2}, {1}})
{{1, 4, 6, 8}, {2, 7, 9}, {3, 10}}

>> Flatten({{{111, 112, 113}, {121, 122}}, {{211, 212}, {221, 222, 223}}}, {{3}, {1}, {2}})
{{{111, 121}, {211, 221}}, {{112, 122}, {212, 222}}, {{113}, {223}}}

>> Flatten({{{1, 2, 3}, {4, 5}}, {{6, 7}, {8, 9,  10}}}, {{3}, {1}, {2}})
{{{1, 4}, {6, 8}}, {{2, 5}, {7, 9}}, {{3}, {10}}}

>> Flatten({{{1, 2, 3}, {4, 5}}, {{6, 7}, {8, 9, 10}}}, {{2}, {1, 3}})
{{1, 2, 3, 6, 7}, {4, 5, 8, 9, 10}}

>> Flatten({{1, 2}, {3,4}}, {1, 2})
{1, 2, 3, 4}
```

Levels to be flattened together in {{-1, 2}} should be lists of positive integers.
```
>> Flatten({{1, 2}, {3, 4}}, {{-1, 2}})
Flatten({{1, 2}, {3, 4}}, {{-1, 2}}, List)
```

Level 2 specified in {{1}, {2}} exceeds the levels, 1, which can be flattened together in {a, b}.
```
>> Flatten({a, b}, {{1}, {2}})
Flatten({a, b}, {{1}, {2}}, List)
```

Check `n` completion
```
>> m = {{{1, 2}, {3}}, {{4}, {5, 6}}}
>> Flatten(m, {2})
{{{1, 2}, {4}}, {{3}, {5, 6}}}
>> Flatten(m, {{2}})
{{{1, 2}, {4}}, {{3}, {5, 6}}}
>> Flatten(m, {{2}, {1}})
{{{1, 2}, {4}}, {{3}, {5, 6}}}
>> Flatten(m, {{2}, {1}, {3}})
{{{1, 2}, {4}}, {{3}, {5, 6}}}
```

Level 4 specified in {{2}, {1}, {3}, {4}} exceeds the levels, 3, which can be flattened together in {{{1, 2}, {3}}, {{4}, {5, 6}}}.
```
>> Flatten(m, {{2}, {1}, {3}, {4}})
Flatten({{{1, 2}, {3}}, {{4}, {5, 6}}}, {{2}, {1}, {3}, {4}}, List)
```
## #251 tests
```
>> m{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
>> Flatten(m, {1})
{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}
>> Flatten(m, {2})
{{1, 4, 7}, {2, 5, 8}, {3, 6, 9}}
>> Flatten(m, {3})
 : Level 3 specified in {3} exceeds the levels, 2, which can be flattened together in {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}.
Flatten({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, {3}, List)
>> Flatten(m, {2, 1})
{1, 4, 7, 2, 5, 8, 3, 6, 9}
Reproduce strange head behaviour
>> Flatten({{1}, 2}, {1, 2})
 : Level 2 specified in {1, 2} exceeds the levels, 1, which can be flattened together in {{1}, 2}.
Flatten({{1}, 2}, {1, 2}, List)
>> Flatten(a(b(1, 2), b(3)), {1, 2}, b)     (* MMA BUG: {{1, 2}} not {1, 2}  *)
 : Level 1 specified in {1, 2} exceeds the levels, 0, which can be flattened together in a(b(1, 2), b(3)).
Flatten(a(b(1, 2), b(3)), {1, 2}, b)
>> Flatten({{1, 2}, {3, {4}}}, {{1, 2}})
{1, 2, 3, {4}}
>> Flatten({{1, 2}, {3, {4}}}, {{1, 2, 3}})
 : Level 3 specified in {{1, 2, 3}} exceeds the levels, 2, which can be flattened together in {{1, 2}, {3, {4}}}.
Flatten({{1, 2}, {3, {4}}}, {{1, 2, 3}}, List)
>> Flatten(p(1, p(2), p(3)))
p(1, 2, 3)
>> Flatten(p(1, p(2), p(3)), 2)
p(1, 2, 3)
```