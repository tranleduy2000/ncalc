## Take

```
Take(expr, n)
```
> returns `expr` with all but the first `n` leaves removed.

### Examples

```
>> Take({a, b, c, d}, 3)
{a,b,c}

>> Take({a, b, c, d}, -2)
{c,d}

>> Take({a, b, c, d, e}, {2, -2})
{b,c,d}
```

Take a submatrix:

```
>> A = {{a, b, c}, {d, e, f}}
>> Take(A, 2, 2)
{{a,b},{d,e}}
```

Take a single column:

```
>> Take(A, All, {2})
{{b},{e}}

>> Take(Range(10), {8, 2, -1})
{8,7,6,5,4,3,2}

>> Take(Range(10), {-3, -7, -2})
{8,6,4}
```

Cannot take positions `-5` through `-2` in `{1, 2, 3, 4, 5, 6}`.

```
>> Take(Range(6), {-5, -2, -2})
Take({1, 2, 3, 4, 5, 6}, {-5, -2, -2})
```

Nonatomic expression expected at position `1` in `Take(l, {-1})`.

```
>> Take(l, {-1})
Take(l,{-1})
```

Empty case

```
>> Take({1, 2, 3, 4, 5}, {-1, -2})
{}

>> Take({1, 2, 3, 4, 5}, {0, -1})
{}

>> Take({1, 2, 3, 4, 5}, {1, 0})
{}

>> Take({1, 2, 3, 4, 5}, {2, 1})
{}

>> Take({1, 2, 3, 4, 5}, {1, 0, 2})
{}
```

Cannot take positions `1` through `0` in `{1, 2, 3, 4, 5}`.

```
>> Take({1, 2, 3, 4, 5}, {1, 0, -1})
Take({1, 2, 3, 4, 5}, {1, 0, -1})
```