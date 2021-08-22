# compiler
[compiler for c++ and java](https://github.com/fightinggg/compiler)

# pdk
[github](https://github.com/fightinggg/compiler)
[dockerhub](https://hub.docker.com/r/fightinggg/pdk)

## pdk1.0.0 preview
step1. run pdk containe
```shell
$ docker run -it fightinggg/pdk:1.0.0 bash
```

step2. look the pava code
```shell
$ cat code.pava
int fib(int x){
    if(x<2) return 1;
    return fib(x-1) + fib(x-2);
}
int main(){
    int a = fib(5);
    return a;
}
```

step3. look the pavac
```shell
$ java -jar pavac.jar
```
then you can see this output
```txt
welcome to pavac 1.0 ...
you can compile pavac code like this :
$ java -jar pavac.jar -pava code.pava
you can debug pava code like this :
$ java -jar pavac.jar -pava code.pava -debug
you can output to file like this :
$ java -jar pavac.jar -pava code.pava -output code.par
```

step4. compile pava code using pavac
```shell
$ java -jar pavac.jar -pava code.pava -output code.par
```

step5. look the PVM (Pava Virtual Machine)
```shell
$ java -jar pava.jar
```
then you can see this output 
```txt
welcome to pava 1.0 ...
you can run pava code like this :
$ java -jar pava.jar -par code.par
you can debug pava code like this :
$ java -jar pava.jar -par code.par -debug
```

step6. run par code using Pava Virtual Machine
```shell
$ java -jar pava.jar -par code.par
```
then you can see the output 
```txt
return: 8
```

so , is it amazing ? the code code.pava is a pava code and it compute the fifth Fibonacci Number,

Fibonacci Number is 1 2 3 5 8 13 21 34 ...

enjoy the pdk 1.0.0 

for more study about pdk , just see [fightinggg's blog](https://fightinggg.github.io/QV7MPO.html)