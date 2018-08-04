## LinearRecurrence

```
LinearRecurrence(list1, list2, n)
```

> solve the linear recurrence and return the generated sequence of elements.

```
LinearRecurrence(list1, list2, {n})
```

> solve the linear recurrence and return the `n`-th element of the list.

See:
* [Wikipedia - Recurrence relation](https://en.wikipedia.org/wiki/Recurrence_relation)
* [OEIS - Recurrence - Linear recurrences with constant coefficients](http://oeis.org/wiki/Recurrence#Linear_recurrences_with_constant_coefficients)

### Examples
 
The [A001608 Perrin](https://oeis.org/A001608) integer sequence

```
>> LinearRecurrence({0, 1, 1}, {3, 0, 2}, 50) 
{3,0,2,3,2,5,5,7,10,12,17,22,29,39,51,68,90,119,158,209,277,367,486,644,853,1130,1497,1983,2627,3480,4610,6107,8090,10717,14197,18807,24914,33004,43721,57918,76725,101639,134643,178364,236282,313007,414646,549289,727653,963935}
```

The [A016064 Shortest legs of Heronian triangles (sides are consecutive integers, area is an integer)](https://oeis.org/A016064) integer sequence


```
>> LinearRecurrence({5, -5, 1}, {1, 3, 13}, 26) 
{1,3,13,51,193,723,2701,10083,37633,140451,524173,1956243,7300801,27246963,101687053,379501251,1416317953,5285770563,19726764301,73621286643,274758382273,1025412242451,3826890587533,14282150107683,53301709843201,198924689265123}
```

The [A251599 Centers of rows of the triangular array formed by the natural numbers](https://oeis.org/A251599) integer sequence

```
>> LinearRecurrence({1, 0, 2, -2, 0, -1, 1}, {1, 2, 3, 5, 8, 9, 13}, 60) 
{1,2,3,5,8,9,13,18,19,25,32,33,41,50,51,61,72,73,85,98,99,113,128,129,145,162,163,181,200,201,221,242,243,265,288,289,313,338,339,365,392,393,421,450,451,481,512,513,545,578,579,613,648,649,685,722,723,761,800,801}
```

The [A050250 Number of nonzero palindromes less than 10^n](https://oeis.org/A050250) integer sequence

```
>> LinearRecurrence({1, 10, -10}, {9, 18, 108}, 30) 
{9,18,108,198,1098,1998,10998,19998,109998,199998,1099998,1999998,10999998,19999998,109999998,199999998,1099999998,1999999998,10999999998,19999999998,109999999998,199999999998,1099999999998,1999999999998,10999999999998,19999999999998,109999999999998,199999999999998,1099999999999998,1999999999999998}
```
