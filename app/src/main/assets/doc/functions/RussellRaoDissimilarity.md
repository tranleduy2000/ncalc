## RussellRaoDissimilarity

```
RussellRaoDissimilarity(u, v)
``` 

> returns the Russell-Rao dissimilarity between the two boolean 1-D lists `u` and `v`, which is defined as `(n - c_tt) / c_tt` where `n` is `len(u)` and `c_ij` is the number of occurrences of `u(k)=i` and `v(k)=j` for `k<n`.
  
### Examples
``` 
>> RussellRaoDissimilarity({1, 0, 1, 1, 0, 1, 1}, {0, 1, 1, 0, 0, 0, 1})
5/7
``` 