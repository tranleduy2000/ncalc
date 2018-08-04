## BooleanMinimize

```
BooleanMinimize(expr)
```

> minimizes a boolean function with the [Quine McCluskey algorithm](https://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm)
 
### Examples

```
>> BooleanMinimize(x&&y||(!x)&&y)
y

>> BooleanMinimize((a&&!b)||(!a&&b)||(b&&!c)||(!b&&c))
a&&!b||!a&&c||b&&!c
```