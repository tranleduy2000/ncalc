## BooleanMinimize

```
BooleanMinimize(expr)
```

> minimizes a boolean function with the [Quine McCluskey algorithm](http://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm)
 
### Examples

```
>> BooleanMinimize(x&&y||(!x)&&y)
y
```