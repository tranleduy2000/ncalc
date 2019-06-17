## BinarySerialize

```
BinarySerialize(expr)
```

> serialize the `expr` into a byte array expression.
 
### Examples

```
>> BinarySerialize(f(g,2)) // Normal
{56,58,102,2,115,8,71,108,111,98,97,108,96,102,115,8,71,108,111,98,97,108,96,103,67,2}
```