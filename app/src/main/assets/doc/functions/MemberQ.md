## MemberQ
```
MemberQ(list, pattern)
```
> returns `True` if pattern matches any element of `list`, or `False` otherwise.

### Examples
 
```
>> MemberQ({a, b, c}, b)
True
>> MemberQ({a, b, c}, d)
False
>> MemberQ({"a", b, f(x)}, _?NumericQ)
False
>> MemberQ(_List)({{}})
True
```