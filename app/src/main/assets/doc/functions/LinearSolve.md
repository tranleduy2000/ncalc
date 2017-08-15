## LinearSolve
```
LinearSolve(matrix, right)
```

> solves the linear equation system 'matrix . x = right' and returns one corresponding solution `x`.

### Examples	
```
>> LinearSolve({{1, 1, 0}, {1, 0, 1}, {0, 1, 1}}, {1, 2, 3})
{0,1,2}
```

Test the solution:
```
>> {{1, 1, 0}, {1, 0, 1}, {0, 1, 1}} . {0, 1, 2}
{1,2,3}
```

If there are several solutions, one arbitrary solution is returned:
```
>> LinearSolve({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, {1, 1, 1})
{-1,1,0}
```

Infeasible systems are reported:
```
>> LinearSolve({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, {1, -2, 3})
 : Linear equation encountered that has no solution.
LinearSolve({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, {1, -2, 3})
```

Argument {1, {2}} at position 1 is not a non-empty rectangular matrix.
```
>> LinearSolve({1, {2}}, {1, 2})
LinearSolve({1, {2}}, {1, 2})
>> LinearSolve({{1, 2}, {3, 4}}, {1, {2}}) 
LinearSolve({{1, 2}, {3, 4}}, {1, {2}})
```