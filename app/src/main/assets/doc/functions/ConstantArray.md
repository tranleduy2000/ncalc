## ConstantArray

```
ConstantArray(expr, n)
```
> returns a list of `n` copies of `expr`.

### Examples

```
>> ConstantArray(a, 3)
{a, a, a}
 
>> ConstantArray(a, {2, 3})
{{a, a, a}, {a, a, a}}
```