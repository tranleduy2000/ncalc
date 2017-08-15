## SokalSneathDissimilarity

```
SokalSneathDissimilarity(u, v)
``` 

> returns the Sokal-Sneath dissimilarity between the two boolean 1-D lists `u` and `v`, which is defined as `R / (c_tt + R)` where n is `len(u)`, `c_ij` is the number of occurrences of `u(k)=i` and `v(k)=j` for `k<n`, and `R = 2 * (c_tf + c_ft)`.  
	  
### Examples
``` 
>> SokalSneathDissimilarity({1, 0, 1, 1, 0, 1, 1}, {0, 1, 1, 0, 0, 0, 1})
4/5
```