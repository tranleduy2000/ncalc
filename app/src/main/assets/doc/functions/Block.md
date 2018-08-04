## Block

```
Block({list_of_local_variables}, expr )
```

> evaluates `expr` for the `list_of_local_variables`

### Examples

```
>> $blck=Block({$i=10}, $i=$i+1; Return($i))
11
```

The [A005132 Recaman's sequence](http://oeis.org/A005132) integer sequence

```
>> f(s_List) := Block({a = s[[-1]], len = Length@s}, Append(s, If(a > len && !MemberQ(s, a - len), a - len, a + len))); Nest(f, {0}, 70) 
{0,1,3,6,2,7,13,20,12,21,11,22,10,23,9,24,8,25,43,62,42,63,41,18,42,17,43,16,44,15,45,14,46,79,113,78,114,77,39,78,38,79,37,80,36,81,35,82,34,83,33,84,32,85,31,86,30,87,29,88,28,89,27,90,26,91,157,224,156,225,155}
```