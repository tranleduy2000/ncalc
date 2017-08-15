## PatternTest

```
PatternTest(pattern, test)
```
or
```
pattern ? test
```
> constrains `pattern` to match `expr` only if the evaluation of `test(expr)` yields `True`.

### Examples

```
>> MatchQ(3, _Integer?(#>0&))
True
	 
>> MatchQ(-3, _Integer?(#>0&))
False
```
