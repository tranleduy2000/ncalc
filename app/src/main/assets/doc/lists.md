## Lists

Lists can be entered in Symja with curly braces `{` and `}`:
```
>> mylist = {a, b, c, d}
{a,b,c,d}
```

There are various functions for constructing lists:
```
>> Range(5)
{1,2,3,4,5}
 
>> Array(f, 4)
{f(1),f(2),f(3),f(4)}

>> ConstantArray(x, 4)
{x,x,x,x}

>> Table(n ^ 2, {n, 2, 5})
{4,9,16,25}
```

The number of elements of a list can be determined with `Length`:
```
>> Length(mylist)
4
```

Elements can be extracted using double square braces:
```
>> mylist[[3]]
c
```

Negative indices count from the end:
```
>> mylist[[-3]]
b
```

Lists can be nested:
```
>> mymatrix = {{1, 2}, {3, 4}, {5, 6}};
```

There are various ways of extracting elements from a list:
```
>> mymatrix[[2, 1]]
3

>> mymatrix[[;;, 2]]
{2,4,6}

>> Take(mylist, 3)
{a,b,c}

>> Take(mylist, -2)
{c,d}

>> Drop(mylist, 2)
{c,d}

>> First(mymatrix)
{1,2}

>> Last(mylist)
d

>> Most(mylist)
{a,b,c}

>> Rest(mylist)
{b,c,d}
```

Lists can be used to assign values to multiple variables at once:
```
>> {a, b} = {1, 2};

>> a
1

>> b
2
```

Many operations, like addition and multiplication, “thread” over lists, i.e. lists are combined element-wise:
```
>> {1, 2, 3} + {4, 5, 6}
{5,7,9}

>> {1, 2, 3} * {4, 5, 6}
{4,10,18}
```

It is an error to combine lists with unequal lengths:
```
>> {1, 2} + {4, 5, 6}
Lists of unequal length cannot be combined: {1,2}+{4,5,6}
{1,2}+{4,5,6}
```

 