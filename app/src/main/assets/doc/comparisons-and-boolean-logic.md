## Comparisons and Boolean logic

Values can be compared for equality using the `==` operator:
```
>> 3 == 3
True 

>> 3 == 4
False
```

The special symbols `True` and `False` are used to denote truth values. Naturally, there are inequality comparisons as well:
```
>> 3 > 4
False
```

Truth values can be negated using `!` (logical not) and combined using `&&` (logical and) and `||` (logical or):
```
>> !True
False

>> !False
True

>> 3 < 4 && 6 > 5
True
```

`&&` has higher precedence than `||`, i.e. it binds stronger:
```
>> True && True || False && False
True

>> True && (True || False) && False
False
```