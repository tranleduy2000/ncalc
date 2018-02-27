## Symbols and assignments

Symbols need not be declared in Symja, they can just be entered and remain variable
(Only the symbols which consists of exactly one character are case sensitive. 
For all other identifiers the input parser doesn't distinguish between lower and upper case characters):
```
>> x
x
```

Basic simplifications are performed:
```
>> x + 2 * x
3*x
```

Symbols can have any name that consists of characters and digits:
```
>> iAm1Symbol ^ 2
iam1symbol^2
```

You can assign values to symbols:
```
>> a = 2
2

>> a ^ 3
6

>> a = 4
4

>> a ^ 3
64
```

Assigning a value returns that value. If you want to suppress the output of any result, add a `;` to the end of your query:
```
>> a = 4;
```

Values can be copied from one variable to another:
```
>> b = a;
```

Now changing a does not affect b:
```
>> a = 3;

>> b
4
```

Such a dependency can be achieved by using “delayed assignment” with the `:=` operator 
(which does not return anything, as the right side is not even evaluated):
```
>> b := a ^ 2

>> b
9

>> a = 5;

>> b
25
```