## ContainsOnly

```
ContainsOnly(list1, list2)
```

> yields True if `list1` contains only elements that appear in `list2`.


### Examples

```
>> ContainsOnly({b, a, a}, {a, b, c})
True
```

The first list contains elements not present in the second list:

```
>> ContainsOnly({b, a, d}, {a, b, c})
False

>> ContainsOnly({}, {a, b, c})
True
```



