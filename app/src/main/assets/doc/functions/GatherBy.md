## GatherBy

```
GatherBy(list, f) 
```

> gathers leaves of `list` into sub lists of items whose image under `f` identical.
 
```
GatherBy(list, {f, g,...}) 
```

> gathers leaves of `list` into sub lists of items whose image under `f` identical. Then, gathers these sub lists again into sub sub lists, that are identical under `g`.

### Examples

``` 
>> GatherBy({{1, 3}, {2, 2}, {1, 1}}, Total)
{{{1,3},{2,2}},{{1,1}}}
     
>> GatherBy({"xy", "abc", "ab"}, StringLength)
{{xy,ab},{abc}}
     
>> GatherBy({{2, 0}, {1, 5}, {1, 0}}, Last)
{{{2,0},{1,0}},{{1,5}}}
     
>> GatherBy({{1, 2}, {2, 1}, {3, 5}, {5, 1}, {2, 2, 2}}, {Total, Length})
{{{{1,2},{2,1}}},{{{3,5}}},{{{5,1}},{{2,2,2}}}} 
```