# 类型

## 基本内置类型

所谓**内置类型（built-in type）**，就是语言本身定义的类型。C++定义了一套包括**算术类型（arithmetic type）**与一个特殊的```void```类型在内的原始类型（primitive type）。其中，```void```不关联任何值，并且只能用于一些特殊的场合，最常见的是作为不返回任何值的函数的返回类型。

### 算术类型

在C++中，所有的算术类型分为两类：**整型（integral types）**（包括字符类型与布尔类型）与浮点型（floating-point types）。

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
        <th>尺寸</th>
    </tr>
    <tr>
        <td>bool</td>
        <td>布尔类型。</td>
        <td>未定义，取值为true或false。</td>
    </tr>
    <tr>
        <td>char</td>
        <td>字符。</td>
        <td>最少8位，保证可以存放机器基本字符集中任意字符对应的数值。</td>
    </tr>
    <tr>
        <td>wchar_t</td>
        <td>扩展字符：宽字符。</td>
        <td>最少16位，确保可以存放机器最大扩展字符集中的任意字符。</td>
    </tr>
    <tr>
        <td>char16_t</td>
        <td>扩展字符：Unicode字符，即UTF-16字符表示的类型。</td>
        <td>最少16位。</td>
    </tr>
    <tr>
        <td>char32_t</td>
        <td>扩展字符：Unicode字符，即UTF-32字符表示的类型。</td>
        <td>最少32位。</td>
    </tr>
    <tr>
        <td>short</td>
        <td>短整型。</td>
        <td>最少16位。</td>
    </tr>
    <tr>
        <td>int</td>
        <td>整型。</td>
        <td>最少16位，至少与short一样大。</td>
    </tr>
    <tr>
        <td>long</td>
        <td>长整型。</td>
        <td>最少32位，至少int一样大。</td>
    </tr>
    <tr>
        <td>long long</td>
        <td>长整型。</td>
        <td>最少64位，至少与long一样大。</td>
    </tr>
    <tr>
        <td>float</td>
        <td>单精度浮点数。</td>
        <td>最少6位有效数字，一般32位、7位有效数字。</td>
    </tr>
    <tr>
        <td>double</td>
        <td>双精度浮点数。</td>
        <td>最少6位有效数字，一般64位、16位有效数字。</td>
    </tr>
    <tr>
        <td>long double</td>
        <td>扩展精度浮点数。</td>
        <td>最少10位有效数字，一般96位或128位。</td>
    </tr>
</table>
除了```bool```与扩展字符类型，其他整型分为**有符号的（signed）**与**无符号的（unsigned）**，分别使用前置```signed```与```unsigned```限定。有符号类型可表示正数、负数或0，无符号类型只能表示正数或0。无符号类型的比特序列表示二进制值，C++没有规定有符号类型如何表示，但是约定正值与负值的表示范围应均衡（the range should be evenly divided between positive and negative values）。

除了字符类型，有符号类型的前置```signed```可以省略。若字符类型没有前置```signed```或```unsigned```，则其可能是有符号的，也可能是无符号的，取决于编译器。```unsigned int```可以简写为```unsigned```。

```short```、```int```、```long```、```long long```依次增大（不管是有符号的还是无符号的），无符号类型比对应的有符号类型大。

下面是选择类型的一些建议：

- 当明确数值不可能为负时，使用无符号类型。
- 使用```int```执行整数运算。因为```short```往往过小，而```long```与```int```一般一样大；若数值超过```int```的表示范围，使用```long long```。
- 在算术表达式中不要使用```bool```与```char```，只有在存放布尔值时使用```bool```，存放字符时使用```char```。
- 执行浮点运算时使用```double```，因为```float```精度不够且运算速度相比于```double```没有优势；对于某些机器，双精度运算甚至比单精度运算还快。```long double```提供的精度一般不需要，且运算开销大。

### 字面值

一个形如```42```的值被称为**字面值（literal）**，这样的值一望便知（self-evident）。

#### 整型字面值

整型字面值表示整数，并且前置正号（```+```）（可省略）表示正数，前置负号（```-```）表示负数。

整型字面值可以表示为八进制数、十进制数或十六进制数。以```0```开头的整数代表八进制数，以```0x```或```0X```开头的整数代表十六进制数，否则默认为十进制数。

十进制字面值为有符号数，其类型为```int```、```long```、```long long```中能容纳下该值的最小类型；八进制与十六进制字面值可能为有符号数，也可能为符号数，其类型为```int```、```unsigned int```、```long```、```unsigned long```、```long long```、```unsigned long long```中能容纳下该值的最小类型。如果都不能容纳，则报错。

整型字面值虽然可以存放在有符号类型中，但是十进制字面值不会是负数，符号仅仅是对字面值取负。

在整型字面值后加上特定后缀可指定其最小类型：

<table>
    <tr>
        <th>后缀</th>
        <th>最小类型</th>
    </tr>
    <tr>
        <td>u或U</td>
        <td>unsigned</td>
    </tr>
    <tr>
        <td>l或L</td>
        <td>long</td>
    </tr>
    <tr>
        <td>ll或LL</td>
        <td>long long</td>
    </tr>
</table>
其中```u```或```U```后缀可与其他两后缀结合使用。

#### 浮点型字面值

浮点型字面值使用小数表示或科学计数法表示，其中指数部分使用```E```或```e```标识（而不是```×10```的表达方式），其后跟指数，且指数必须为整数。浮点型字面值默认为```double```，若字面值超过```double```的表示范围，则报错。若浮点型整数部分为```0```，则```0```可省略；小数部分为```0```，则小数部分可省略（可以小数点不省略）。

在浮点型字面值后加上特定后缀可指定其类型（同样地，字面值超过该类型表示范围会报错）：

<table>
    <tr>
        <th>后缀</th>
        <th>类型</th>
    </tr>
    <tr>
        <td>f或F</td>
        <td>float</td>
    </tr>
    <tr>
        <td>l或L</td>
        <td>long double</td>
    </tr>
</table>
#### 字符字面值

由单引号括起来的一个字符被称为```char```型字面值。

##### 转义序列

由两类字符不能被直接表示：不可打印字符，如退格或其他控制字符；在C++中有特殊含义的字符（单引号、双引号、问号与反斜线）。在这些情况下需要用到**转义序列（escape sequence）**表示这些特定的字符，转义序列以```\```开始，其后跟一个字符（必须是特定的字符）。

C++定义了以下转义序列：

```c++
'\n' /* 换行符 */, '\t' /* 横向制表符 */, '\a' /* 报警（响铃）符 */, '\v' /* 纵向制表符 */, '\b' /* 退格符 */, '\"' /* 双引号 */, '\\' /* 反斜线 */, '\?' /* 问号 */, '\'' /* 单引号 */, '\r' /* 回车符 */, '\f' /* 进纸符 */
```

泛化的转义序列以反斜线开始，其后跟1~3个八进制数字（如果超过3个，只有前3个构成转义序列的一部分）或以```x```开始的1个或多个十六进制数字（超过表示范围也会报错）。其中数字表示字符对应的数值。

```c++
'\7' /* 响铃符 */, '\12' /* 换行符 */, '\40' /* 空格 */, '\0' /* 空字符 */, '\115' /* 字符M */, '\x4d' /* 字符M */
```

在字符字面值前（单引号前）加上特定前缀可指定其类型：

<table>
    <tr>
        <th>前缀</th>
        <th>类型</th>
    </tr>
    <tr>
        <td>u</td>
        <td>char16_t</td>
    </tr>
    <tr>
        <td>U</td>
        <td>char32_t</td>
    </tr>
    <tr>
        <td>L</td>
        <td>wchar_t</td>
    </tr>
</table>
#### 字符串字面值

由双引号括起来的0个或多个字符构成**字符串字面值（string literal）**。

字符串字面值实际上是常量字符构成的数组，编译器在字符串的结尾添加一个空字符（```'\0'```）作为结束标志，因此字符串字面值（或数组）的实际长度比它的内容多1。

若两个字符串字面值位置紧邻且仅由（0个或多个）空格、缩进或换行符分隔，则其会拼接为一个整体。

```c++
cout << "a really, really long string literal "
        "that spans two lines" << endl;
```

在字符串字面值前（双引号前）加上特定前缀可指定其类型：

<table>
    <tr>
        <th>前缀</th>
        <th>类型</th>
    </tr>
    <tr>
        <td>u8</td>
        <td>utf-8字符串字面值，字符类型为char。</td>
    </tr>
</table>
字符串字面值是**C风格字符串（C-style character string）**，其特征为：字符串存放在字符数组中并以空字符结束（null terminated）。

C风格字符串不支持重载运算操作。

```c++
const char ca1[] = "A string example";
const char ca2[] = "A different string";
if (cal1 < cal2) {  // 错误，比较指针，结果未定义。
    // ...
}
```

以下函数位于```cstring```头文件中，用于操纵C风格字符串，其中```p```、```p1```与```p2```为C风格字符串（如果传入的参数是字符数组，必须以空字符结尾，否则结果未定义）：

<table>
    <tr>
        <th>函数</th>
        <th>功能</th>
    </tr>
    <tr>
        <td>strlen(p)</td>
        <td>返回p的长度（不包括空字符）</td>
    </tr>
    <tr>
        <td>strcmp(p1, p2)</td>
        <td>若p1等于p2则返回0，若p1大于p2则返回一个正整数，若p1小于p2则返回一个负整数。大小比较使用字典序。</td>
    </tr>
    <tr>
        <td>strcat(p1, p2)</td>
        <td>将p2附加到p1后，返回p1。必须保证p1有足够的空间存放拼接后的结果（包括空字符）。注意C风格字符串不支持使用“+”拼接</td>
    </tr>
    <tr>
        <td>strcpy(p1, p2)</td>
        <td>将p2拷贝给p1，返回p1。必须保证p1有足够的空间存放p2（包括空字符）。注意C风格字符串不支持使用“=”拷贝。</td>
    </tr>
</table>
```c++
const char ca1[] = "A string example";
const char ca2[] = "A different string";

// 拼接ca1与ca2，结果存放进字符数组largeStr。
// 必须保证largeStr足够大。
strcpy(largeStr, ca1);
strcat(largeStr, " ");
strcat(largeStr, ca2);
```

#### 布尔字面值

即```bool```类型的字面值，包括```true```与```false```。

#### 指针字面值

```nullptr```，用于定义空指针。

## 复合类型

**复合类型（compound type）**指基于其他类型定义的类型。

### 引用

**引用（reference）**为对象起了另外一个名字。

引用必须被初始化，程序将引用和其初始值**绑定（bind）**在一起。这种绑定关系是不能改变的，一旦被初始化，左值引用就无法绑定其他对象了。

定义了一个引用后，对其进行的所有操作都是在与之绑定的对象上进行的。

引用不是对象，无法定义引用的引用。

#### 左值引用

通过```&```类型修饰符来定义**左值引用（lvalue reference）**，左值引用就是必须绑定到左值的引用。

一般可将左值引用简称为引用。

因为引用即别名，所以引用只能被绑定在对象上，而不能与字面值或表达式计算结果绑定。

由于对引用的所有操作都是在与之绑定的对象上进行的，因此如果用一个左值引用初始化另一个引用，实际上是用前一个引用绑定的对象初始化后一个引用。

```c++
int ival = 1024;
int &refVal = ival;
int &refVal2;  // 错误，引用必须初始化。
refVal = 2;  // 将2赋值给refVal绑定的对象，即ival。
int ii = refVal;  // 等价于ii = ival。
int &refVal3 = refVal;  // 正确，实际上将refVal3绑定到refVal绑定的对象，即ival。
int i = refVal;
```

除了底层```const```转换与派生类到基类的转换外，左值引用的类型必须与之绑定的对象类型严格匹配。

```c++
int &refVal4 = 10;
double dval = 3.14;
int &refVal5 = dval;
```

#### 右值引用

通过```&&```类型修饰符来定义**右值引用（rvalue reference）**，右值引用就是必须绑定到右值的引用。左值有持久的状态，而右值要么是字面值，要么是表达式求值过程中创建的临时对象，因此右值引用引用的对象即将要销毁且该对象没有其他用户，使用右值引用的代码可以自由地接管引用的对象的资源。

要注意变量是左值（即使变量是右值引用），因此不能将右值引用绑定到变量上。

```c++
int i = 42;
int &r = i;  // 正确。
int &rr = i;  // 错误，i是左值。
int &r2 = i * 42;  // 错误，i * 42为右值。
const int &r3 = i * 42;  // 正确，可以将常量引用绑定到右值。
int &&rr2 = i * 42;  // 正确。

int &&rr1 = 42;
int &&rr2 = rr1;  // 错误，rr1是左值。
```

#### 引用处理

##### ```std::forward```

```std::forward```函数定义在头文件```utility```中，其接受一个显式模板实参，并返回该显式实参类型的右值引用。即：```std::forward<T>```的返回类型是```T&&```，```std::forward<T>(t)```返回```T&&```类型的```t```。

##### ```std::move```

```move```函数定义在头文件```utility```中，用于将左值实参转换为对应的右值引用类型，其定义如下：

```c++
// 形参为T&&，可以接受任何类型的实参。
// 如果实参为右值，则T为非引用类型。
// 如果实参为左值，则T为左值引用类型。
// 可以使用static_cast显式地将左值引用转换为右值引用（隐式转换不被允许）。
// 不管如何，最终都返回t的右值引用。
template <typename T> typename remove_reference<T>::type &&move(T &&t) {
    return static_cast<typename remove_reference<T>::type&&>(t);
}
```

##### 移动迭代器

**移动迭代器（move iterator）**通过改变迭代器解引用运算符的行为来适配给定的迭代器。通常，迭代器解引用运算符返回元素的左值引用，而移动迭代器的解引用运算符生成一个右值引用。

通过调用标准库的```make_move_iterator```函数将一个普通迭代器（作为实参）转换为一个移动迭代器（作为返回值）。

```c++
// 重写StrVec::reallocate，使用移动迭代器“拷贝”元素。
void StrVec::reallocate() {
    auto newcapacity = size() ? 2 * size() : 1;
    auto first = alloc.allocate(newcapacity);
    auto last = uninitialized_copy(make_move_iterator(begin()), make_move_iterator(end()), first);  // construct将使用移动构造函数来构造元素。
    free();
    elements = first;
    first_free = last;
    cap = elements + newcapacity;
}
```

标准库并不保证哪些算法可以使用移动迭代器。因为移动一个对象可能销毁源对象，因此只有当确信算法在为一个元素赋值或将其传递给一个用户自定义的函数后不再访问它时，才能将移动迭代器传递给算法。

### 指针

**指针（pointer）**是“指向”另外一种类型的复合类型，指针的值就是一个对象在内存中的地址。指针本身是一个对象，可以不在定义时赋初始值。

通过```*```类型修饰符来定义指针。

```c++
int *ip1, ip2;
double dp, *dp2;  // dp为double，不是指针。
```

通过对变量前置```&```（**取地址符（& operator）**）来获得对象的地址（是一个右值）。

```c++
int ival = 42;
int *p = &ival;
```

指针的值（即地址）属于以下四种状态之一：

1. 指向一个对象。
2. 指向紧邻对象所占空间的下一位置。
3. 空指针，不指向任何对象。
4. 无效指针，即上述情况外的其他值。

无效指针的值不能被访问，否则结果是未定义的。2、3种形式的指针没有指向任何具体对象，因此试图访问此类指针指向的（假定的）对象的行为不被允许，否则结果是未定义的。

访问未初始化的指针的行为也是未定义的，尤其是，如果指针所指向的内存空间中恰好有内容，而这些内容又被当作指针，则很难分清它到底是否合法。因此建议初始化所有指针，并尽量在定义了对象后再定义指向它的指针。如果不清楚指针指向何处，则将其初始化为空指针。

指针是随机访问迭代器。指针的大小为其地址值的大小；对指针加上或减去一个整数实际上是对指针的地址值加上或减去该整数；两指针的差为其地址值之差；不允许对两指针求和。一般情况下，对于指向不相关的对象的两个指针执行这些运算是没有意义的。指针运算一般用于数组。

如果指针指向了对象，通过使用```*```（**解引用符（* operator）**）来获得该对象（左值）。

```c++
int ival = 42;
int *p = &ival;
cout << *p << endl;
```

除了底层```const```转换与派生类到基类的转换外，指针的类型必须与其指向的对象类型严格匹配。

```c++
double dval;
double *pd = &dval;
double *pd2 = pd;

int *pi = pd;  // 错误，指针类型不匹配。
pi = &dval;  // 错误，将double的地址赋给int指针。
```

#### 空指针

**空指针（null pointer）**不指向任何对象。可以通过使用字面值```nullptr```、```0```或预处理变量```NULL```来初始化或赋值指针使其成为空指针，其中```NULL```定义在头文件```cstdlib```中。最好使用```nullptr```且避免使用```NULL```。

```c++
int *p1 = nullptr;
int *p2 = 0;
int *p3 = NULL;  // 需要先#include <cstdlib>。
```

不能将算术类型对象赋值给指针，即使其值刚好为0。

```c++
int zero = 0;
pi = zero;  // 错误。
```

指针运算时，空指针的地址值相当于为0。空指针可以加上或减去值为0的常量表达式。

#### ```void*```指针

```void*```可以存放任意对象的地址，但是对该地址中存放的对象的类型未知，它仅仅表示内存中的一个地址，因此不能操作```void*```指向的对象。

```c++
double obj = 3.14, *pd = &obj;
void *pv = &obj;  // 正确，obj可以是任意类型对象。
pv = pd;  // 正确，pv可以保存指向任意类型的指针。
```

#### 指向指针的指针

因为指针是对象，所以可以定义指向指针的指针。

```c++
int ival = 1024;
int *pi = &ival;  // pi指向ival。
int **ppi = &pi;  // ppi指向pi。
cout << ival << " " << *pi << " " << **ppi << endl;  // 使用三种方式输出ival的值。
```

#### 指向指针的引用

无法定义指向引用的指针，因为引用不是对象，但是可以定义对指针的引用，此时引用与指针绑定。

```c++
int i = 42;
int *p;
int *&r = p;

r = &i;  // 相当于p = &i。
*r = 0;  // 相当于*p = 0。
```

### 数组

数组用于存放数量固定的、内存中连续存放的相同类型的一组元素。

#### 定义与初始化

数组的定义方式为：```type arr[size]```。其中，```type```为数组存放元素的类型，不允许使用```auto```推断其类型；```arr```为数组名；```size```为数组维度，必须为常量表达式；该数组的类型为```type[size]```，表示数组存放```size```个类型为```type```的元素。

数组的元素为对象，因而不存在元素为引用的数组。

```c++
int arr[10];  // 含有10个整型的数组。
int *ptrs[10];  // 含有10个整型指针的数组。
int &refs[10] = /* ... */;  // 错误，不存在引用的数组。
int (*Parray)[10] = &arr;  // Parray指向一个含有10个整数的数组。
int (&arrRef)[10] = arr;  // addRef引用一个含有10个整数的数组。
int *(&arry)[10] = ptrs;  // arry引用一个含有10个整型指针的数组。
```

默认情况下，数组中的元素被默认初始化。

可以对数组的元素进行列表初始化，列表中的每个值用来初始化对应的元素，其数量不能超过数组维度（可以为空）；若其数量小于数组维度，则数组中剩下的元素被值初始化。

当对数组使用列表初始化时，可以省略维度```size```，此时数组维度会根据初始值的数量计算并推测出来。不允许创建维度为0的数组。

对于字符数组，可以使用字符串字面值初始化（可以使用花括号包围，也可以不用）。注意编译器在字符串的结尾添加一个空字符（```'\0'```），因此字符串字面值（或数组）的实际长度比它的内容多1。

```c++
char a1[] = {'C', '+', '+'};  // 维度为3的数组。
char a2[] = {'C', '+', '+', '\0'};  // 维度为4的数组，以空字符结尾。
char a3[] = "C++";  // 维度为4的数组，自动添加了空字符。
const char a4[6] = "Daniel";  // 错误，没有空间存放空字符。
```

不允许对数组拷贝或赋值。

```c++
int a[] = {0, 1, 2};
int a2[] = a;  // 错误。
a2 = a;  // 错误。
```

#### 访问数组元素

通过下标运算符访问数组元素，下标运算符的参数为元素的位置，从0开始。下标的类型为```size_t```，定义在```cstddef```头文件中，是一种机器相关的无符号类型，可以存放内存中任意对象的大小。

数组的下标不能越界，否则结果未定义。

```c++
// 记录各分数段的成绩个数。
unsigned scores[11] = {};
unsigned grade;
while (cin >> grade) {
    if (grade <= 100) {
         ++scores[grade/10];
    }   
}
```

通过范围```for```语句遍历数组元素。

```c++
for (auto i : scores) {
    cout << i << " ";
}
cout << endl;
```

#### 指针与数组

和一般对象一样，对数据元素取地址符能得到其指向的指针。在大多数情况下，当使用数组名的时，编译器自动将其替换为一个指向数组首元素的指针。

```c++
string nums[] = {"one", "two", "three"};
string *p2 = nums;  // 等价于：string *p2 = &nums[0]。
```

当使用数组作为```auto```变量的初始值时，得到的类型是指针。

```c++
int ia[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
auto ia2(ia);  // ia2为int*，指向ia的第一个元素。
ia2 = 42;  // 错误。
```

当使用数组作为```decltype```的参数时，得到的是数组类型。

```c++
decltype(ia) ia3 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};  // ia为int[10]。
ia3 = p;  // 错误。
ia3[4] = i;
```

#### 指针与迭代器

指向数组元素的指针是随机访问迭代器。只有当指针指向数组中的某个元素或数组尾元素的下一位置（尾后指针）时，该指针才是合法的；只有当两指针指向同一数组，比较其大小才是有意义的。尾后指针不能递增或解引用。

```c++
int arr[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
int *p = arr;  // p指向arr[0]。
++p;  // p指向arr[1]。
int *p2 = arr + 4;  // p2指向arr[4]。
int *e = &arr[10];  // e指向数组尾元素的下一位置。
int *e2 = arr + 20;  // 错误。
```

通过指针操作可以遍历数组元素。

```c++
for (int *b = arr; b != e; ++b)
    cout << *b << endl;
```

标准库提供```begin```与```end```函数，通过将数组名作为参数，分别返回指向数组首元素的指针与尾后指针。

```c++
int *pbeg = begin(arr), *pend = end(arr);
while (pbeg != pend && *pbeg >= 0)
    ++pbeg;
```

两指针相减的结果类型是名为```ptrdiff_t```的标准库类型，是有符号类型。

对数组执行下标运算其实就是对指向数组元素的指针执行下标运算，因此数组的下标索引可以是有符号类型（与顺序容器的下标运算不同）。

```c++
int i = ia[2];
int *p = ia;
i = *(p + 2);  // 等价于i = ia[2];
```

```c++
int *p = &ia[2];
int j = p[1];  // 等价于j = *(p + 1)，为ia[3]。
int k = p[-2];  // 等价于k = *(p-2)，为ia[0]。
```

#### 多维数组

严格来说，C++中没有多维数组，通常所说的多维数组指的是数组的数组。

多维数组的定义如下：```type arr[size_1][size_2]...[size_n]```。其定义一个$n$维数组。数组的形状为$size\_1×size\_2×...×size\_n$。实际上这是一个含有$size\_1$元素的数组，其元素为含有$size\_2$个元素的数组，以此类推。

多维数组的初始化方式与一维数组相同，每个元素采用与其类型匹配的初始化方式。

```c++
int ia[3][4] = {
    {0, 1, 2, 3},
    {4, 5, 6, 7},
    {8, 9, 10, 11},
};

int sa[2][3] = {"ab", "cd"};
```

多维数组可以省略掉某个数组元素的花括号，此时用于初始化的值将按照顺序依次初始化该数组内的最后一个维度的基本元素。

类似一维数组，多维数组初始化没有必要包含所有值，此时其他值全部被默认初始化。

```c++
int arr[10][20][30] = {0};  // 所有元素初始化为0。

int ix[2][2][2] = {{1, 2}, {3, 4, 5}};  // ix[0][0][0] = 1, ix[0][0][1] = 2, ix[0][1][0] = 3, ix[0][1][1] = 4, ix[1][0][0] = 5,其他3个基本元素为0。
```

当使用默认值初始化数组时，数组的第一维大小可以省略，但是第二维开始的所有大小都不能省略，因为其均为数组类型的一部分。

```c++
const char[][6] = {"hello", "world"};
```

多维数组与一维数组没有本质区别，只是其元素为数组，因此其操作与一维数组一致。

```c++
ia[2][3] = arr[0][0][0];  // 基本元素赋值。
int (&row)[4] = ia[1];  // 将row绑定到ia[1]上，它是一个包含4个元素的一维数组。
int (*p)[4] = ia;  // ia自动转换为指向ia[0]的指针。注意*p两端的圆括号不能省略，否则p成为元素为整型指针的数组。
```

```c++
// 通过下标实现元素遍历
constexpr size_t rowCnt = 3, colCnt = 4;
int ia[rowCnt][colCnt];
for (size_t i = 0; i != rowCnt; ++i)
    for (size_t j = 0; j != colCnt; ++j)
        ia[i][j] = i * colCnt + j;
```

```c++
// 通过范围for实现元素遍历
size_t cnt = 0;
for (auto &row : ia) {  // row为int(&)[4]。
    for (auto &col : row) {
        col = cnt;
        ++cnt;
    }
}
```

通过范围```for```遍历元素时，如果要改变元素的值，当然要将元素设置为引用。如果要遍历元素，并且使用```auto```自动推断类型，则除了最后一个维度外，其他维度必须设置为引用，否则```auto```自动将数组转换为指针，从而导致编译错误。

```C++
// 通过指针运算实现元素遍历
for (auto p = ia; p != ia + 3; ++p) {  // p为int(*)[4]。
    for (auto q = *p; q != *p + 4; ++q)  // q为int*。
        cout << *q << ' ';
    cout << endl;
}
```

```C++
// 通过begin与end实现元素遍历
for (auto p = begin(ia); p != end(ia); ++p) {  // p为int(*)[4]。
    for (auto q = begin(*p); q != end(*p); ++q)  // q为int*
        cout << *q << ' ';
    cout << endl;
}
```

```c++
// 使用类型别名简化多维数组的指针
using int_array = int[4];  // 等价于：typdef int int_array[4]。
for (int_array *p = ia; p != ia + 3; ++i) {
    for (int *q = *p; q != *p + 4; ++q)
        cout << *q << ' ';
    cout << endl;
}
```

## 枚举

**枚举（enumeration）**用于将一组整型常量组织在一起。与类类似，每个枚举类型定义了一种新的类型。枚举是字面值类型。

C++包含两类枚举：**限定作用域的枚举（scoped enumeration）**与**不限定作用域的枚举类型（unscoped enumeration）**。

枚举成员通过作用域运算符访问其成员。

枚举成员是```const```的，并且只能用常量表达式初始化枚举成员。因此每个枚举成员本身就是常量表达式，可以用于任何需要常量表达式的地方，例如定义枚举类型的```constexpr```变量、作为```switch```语句的表达式、将枚举成员作为```case```标签、作为非类型模板形参或在类的定义中初始化枚举类型的静态数据成员等。

```c++
enum class intTypes { charTyp = 8, shortTyp = 16, intTyp = 16, longTyp = 32, long_longTyp = 64 };
constexpr intTypes charbits = intTypes::charTyp;  // 可以定义枚举类型的constexpr变量。
```

尽管每个枚举定义了唯一的类型，但枚举是由某种内置整型表示的。可以在```enum```名字后加上```:```以及整型类型表示该整型。

```c++
enum intValues : unsigned long long { charTyp = 255, shortTyp = 65535, intTyp = 65535, longTyp = 4294967295UL, long_longTyp = 18446744073709551615ULL };
```

> Being able to specify the underlying type of an ```enum``` lets us control the type used across different implementations. We can be confident that our program compiled under one implementation will generate the same code when we compile it on another.

可以为枚举成员赋值。默认情况下，如果没有为枚举成员赋值，则当前枚举成员的值等于前一个枚举成员的值加1，并且第一个枚举成员的值默认为0。

枚举可以前置声明（forward declare an ```enum```），此时必须（显式或隐式地）指定枚举的底层（underlying）大小。枚举的所有声明与定义必须一致（包括底层大小与是否限定作用域）。

```c++
enum intValues: unsigned long long;
enum class open_modes;  // 限定作用域的枚举默认使用int。
```

```c++
enum class intValues;  // 错误，本来是不限定作用域的枚举，并且底层大小也不同。
enum intValues;  // 错误，前一个是限定作用域的枚举。
enum intValues : long;  // 错误，前一个（隐式）声明为int。
```

要想初始化一个枚举对象或为其赋值，必须使用该类型的枚举成员或另一个该类型的对象。

```c++
open_modes om = 2;  // 错误。
om = open_modes::input;
```

```c++
enum Tokens { INLINE = 128, VIRTUAL = 129 };
void ff(Tokens);
void ff(int);
int main() {
    Tokens curTok = INLINE;
    ff(128);  // 精确匹配ff(int)。即使128恰好等于Tokens::INLINE，也不能将Tokens::INLINE作为实参。
    ff(INLINE);  // 精确匹配ff(Tokens)。
    ff(curTok);  // 精确匹配ff(Tokens)。
    return 0;
}
```

### 限定作用域的枚举

定义限定作用域的枚举的形式是：首先是```enum class```或```enum struct```，然后是枚举类型名，最后是花括号包围的**枚举成员（enumerator）**列表（以分号结束）。

```c++
enum class open_modes { input, output, append };
```

在限定作用域的枚举中，枚举成员名遵循常规的作用域规则，在枚举作用域外不可访问。

默认情况下，限定作用于的枚举的底层类型为```int```。如果指定了枚举成员的底层类型（包括对不限定作用域的枚举的隐式指定），一旦某个枚举成员的值超出了该类型所能容纳的范围，则引发错误。

### 不限定作用域的枚举

定义不限定作用域的枚举的形式是：首先是```enum```，然后是枚举类型名，最后是花括号包围的枚举成员列表（以分号结束）。此时枚举类型名是可选的。

```c++
enum color { red, yellow, green };
enum { floatPrec = 6, doublePrec = 10, double_double_prec = 10 };  // 未命名的不限定作用域的枚举。
```

如果枚举是未命名的，则只能在定义该枚举时定义它的对象，即在```enum```定义的右侧花括号与最后的分号间提供逗号分隔的声明列表（与类类似）。

在不限定作用域的枚举中，枚举成员名的作用域与枚举类型本身的作用域相同。

```c++
enum color { red, yellow, green };
enum stoplight { red, yellow, green };
enum class peppers { red, yello, green };
color eyes = green;  // 正确。
peppers p = green;  // 错误。
color hair = color::red;  // 正确，允许显式访问。
peppers p2 = peppers::red;  // 正确。
```

不限定作用域的枚举没有默认的底层类型，只能确定该类型足够大，可以容纳枚举成员值。

一个不限定作用域的枚举类型的对象或成员可以自动转换为整型，因此可以用在任何需要整型值的地方。

```c++
int i = color::red;
int j = peppers:: red;  // 错误，限定作用域的枚举不会进行隐式转换。
```

当将一个不限定作用域的枚举类型的对象或枚举成员传递给整型形参时，枚举值将被整型提升，提升后的类型由枚举类型的底层类型决定。

```c++
void newf(unsigned char);
void newf(int);
unsigned char uc = VIRTUAL;  //VIRTUAL为enum Tokens的成员。
newf(VIRTUAL);  // 调用newf(int)。不管Tokens的潜在类型是什么（可以是unsigned char，因为可以保存最大值129），也不会调用newf(unsigned char)。
newf(uc);  // 调用newf(unsigned char)。
```

## ```const```限定符（qualifier）

使用前置```const```对变量类型限定可以使得变量成为只读的。因为```const```对象一旦创建后就无法写，所以```const```对象必须被初始化。

```c++
const int bufSize = 512;  // 或int const = 512。
```

当```const```对象使用编译时常量（compile-time constant）初始化时，编译器在编译过程中把用到该变量的地方都替换成对应的值。为了执行上述替换，编译器必须知道变量的初始值，如果程序包含多个文件，则每个用到了```const```对象的文件都必须能访问其初始值，因此变量必须在每个文件中都有其定义。为了支持上述做法且避免对同一变量的重复定义，默认情况下，```const```对象仅在文件内有效。当多个文件中出现同名的```const```变量，等同于在不同文件中定义了独立的变量。

如果```const```变量的初始值不是常量表达式，通过在声明与定义时对```const```变量前置```extern```，使得```const```变量在文件间共享，即在一个文件中定义```const```对象，在其他文件中可以声明并使用之。

```c++
// 定义在file_1.cpp中，注意没有使用常量表达式初始化。
extern const int bufSize = fcn();

// 定义在file_1.h头文件中。
extern const int bufSize;
```

### 对常量的引用

可以把引用绑定到```const```对象上，称为**对常量的引用（reference to ```const```）**，或简称为常量引用（```const``` reference）（严格来说，不存在常量引用，因为引用不是对象，无法让引用成为```const```的）。

常量引用的初始化对象可以是任意表达式，只要其结果能够转换为引用的类型。此时，引用与一个**临时量（temporary）**对象（编译器用来暂存求值结果的临时创建的未命名对象）绑定。

```c++
int i = 42;
const int &r1 = i;
const int &r2 = i;
const int &r3 = r1 * 2;
int &r4 = ri * 2;  // 错误。
```

```c++
double dval = 3.14;
const int &ri = dval;

/* 编译器将上述代码变为类似如下的形式：
 * const int temp = dval;
 * const int &ri = temp;
 */
```

### 指针与```const```

#### 指向常量的指针

**指向常量的指针（pointer to ```const```）**不能用来改变其所指对象的值，可以不被初始化。

通过对指针定义的其他部分前置```const```定义指向常量的指针。

```c++
const double pi = 3.14;
double *ptr = &pi;  // 错误。
const double *cptr = &pi;  // 指向常量的指针。
*cptr = 42;  // 错误，不能给*cptr赋值。
```

#### 常量指针

**常量指针（```const``` pointer）**本身是一个常量。常量指针必须被初始化。

通过对变量名前置```const```限定符定义常量指针。

```c++
int errNumb = 0;
int *const curErr = &errNumb;  // 常量指针。
const double pi = 3.14159;
const double *const pip = &pi;  // 指向常量对象的常量指针。
```

常量指针能否改变其所指向的对象的值取决于对象的类型。

```c++
*pip = 2.72;  // 错误，pip指向常量。
if (*curErr) {
    errorHander();
    *curErr = 0;  // 正确。
}
```

### 顶层```const```与底层```const```

**顶层```const```（top-level ```const```）**限定了变量本身必须为常量，**底层```const```（low-level ```const```）**限定了变量关联的变量是常量（指变量本身不能修改关联的变量的值）。一般地，如果```const```修饰的是变量本身（声明时紧邻变量）则其为顶层```const```，否则其为底层```const```。

```c++
int i = 0;
int *const pi = &i;  // 顶层const。
const int ci = 42;  // 顶层const。
const int *p2 = &ci;  // 底层const。
const int *const p3 = p2;  // 左边的const为底层const，右边的const为顶层const。
const int &r = ci;  // 底层const。
```

当执行对象的拷贝时，顶层```const```对此没有影响，参与拷贝的两个对象仅仅是副本关系；可以将没有对应的底层```const```的对象```a```拷贝给有底层```const```的对象```b```，反之不行。如果允许将```b```拷贝给```a```，则```a```可能会改变```b```关联的变量，造成意料之外的结果。

```c++
int *p = p3;  // 错误，p没有底层常量，p3有。
p2 = p3;  // 正确，有共同的底层常量。
p2 = &i;  // 正确。
int &r = ci;  // 错误，没有共同的底层const。
const int &r2 = i;  // 正确。
```

### 常量表达式

**常量表达式（constant expression）**指值不会改变且编译期间就能得到计算结果的表达式。一个对象是不是常量表达式由它的数据类型和初始值共同决定。字面值是常量表达式；如果一个```const```变量使用常量表达式初始化，或使用所有参与运算的变量均为常量表达式的表达式初始化，则该```const```变量为常量表达式。

```c++
const int max_files = 20;  // 常量表达式。
const int limit = max_files + 1;  // 常量表达式。
int staff_size = 27;  // 不是常量表达式。
const int sz = get_size();  // 不是常量表达式，除非get_size是constexpr函数。
```

可以将变量声明为```constexpr```类型以由编译器验证变量的值是否是一个常量表达式。声明为```constexpr```的变量一定是常量，否则编译期间报错。

```c++
constexpr int mf = 20;
constexpr int limit = mf + 1;
constexpr int sz = size();  // 错误，除非size是constexpr函数。
```

常量表达式的值被称为字面值类型（literal type）。算术类型、引用与指针均属于字面值类型。

对于指针，只有当其初始值为空指针或指向存储于某个固定地址中的对象时才能使用```constexpr```初始化；对于引用，只有当其绑定存储于某个固定地址中的对象时才能使用```constexpr```初始化。函数体内的变量不是存放在固定地址中的；函数体外的变量或静态变量存放于固定地址中。

注意```constexpr```限定整个变量的类型为常量表达式，而不是变量关联的对象。

```c++
const int *p = nullptr;  // 指向常量的指针。
constexpr int *q = nullptr;  // 常量指针。
```

当执行```constexpr```对象的拷贝时，其遵循与```const```对象的拷贝相同的规则。

```c++
constexpr int *np = nullptr;  // np为指向int的常量指针，值为空。
int j = 0;  // 必须定义在函数体外。
constexpr int i = 42;  // 必须定义在函数体外。
constexpr const int *p = &i;  // p为常量指针，指向整型常量i。
constexpr int *pi = &j;  // pi为常量指针，指向int j。
```

## 类型处理

### 类型别名

**类型别名（type alias）**是某种类型的同义词。有两种方法可定义类型别名：

- 在变量定义（没有初始化）时，前置```typedef```，将变量定义变成类型别名声明。
- 使用**别名声明（alias declaration）**```using A = B```，将```A```作为类型```B```的类型别名。

类型别名本身代表一种类型，因此可以定义类型别名的类型别名。

```c++
typedef double wages;  // wages是double的同义词。
using wages = double;  // 前一条语句的等价声明。
typedef wages base, *p;  // base是double的同义词，p是double*的同义词。
using base = wages, p = wages*;  // 前一条语句的等价声明。
```

需要注意类型别名作为一个整体为基本数据类型，在类型解析时不能按照书写形式展开。

```c++
typedef char *pstring;
const pstring cstr = 0;  // cstr是一个常量指针，不是：const char *cstr = 0。
const pstring *ps;  // ps是指向常量指针的指针。
```

### ```auto```类型说明符（specifier）

定义变量时，使用```auto```取代变量的类型一部分或全部让编译器通过初始值自动推导变量类型。显然，```auto```定义的变量必须有初始值。

```auto```会用引用的对象的类型作为```auto```的类型。

```c++
int i = 0, &r = i;
auto a = r;  // r为int。
```

```auto```一般会忽略顶层```const```，保留底层```const```。

```c++
const int ci = i, &cr = ci;
auto b = ci;  // b为int。
auto c = cr;  // c为int。
auto d = &i;  // d为int*。
auto e = &ci;  // e为const int*。
```

如果要令```auto```推断出的类型为顶层```const```或引用，需要显式指定。

```c++
const auto f = ci;
auto &g = ci;  // g为const int&。
auto &h = 42;  // 错误，不能将非常量引用绑定到字面值。
const auto &j = 42;  // 可以为常量引用绑定字面值。
```

若```auto```初始化列表中的变量的基本数据类型部分不一致则报错。

```c++
auto item = val1 + val2;
auto i = 0, *p = &i;  // i为整数，p为整型指针。
auto sz = 0, pi = 3.14;  // 错误：sz为int，pi为double。

auto k = ci, &l = i;  // k为int，l为int&。
auto &m = ci, *p = &ci;  // ci为const int&，p为const int*。
auto &n = i, *p2 = &ci;  // 错误：n的基本数据类型为int，p2的基本数据类型为const int。
```

### ```decltpye```类型说明符

通过将表达式作为```decltype```的参数，可以将让编译器返回表达式的类型。在此过程中，编译器分析表达式并得到它的类型，但是不会计算表达式的值。

```decltype```不会忽略顶层```const```与引用。如果表达式（不包括变量）是左值，则返回该表达式类型的引用类型，否则不返回引用类型；如果```decltype```的参数加了（一个或多个）括号，则永远返回引用。

```c++
const int ci = 0, &cj = ci;
decltype(ci) x = 0;  // x为const int。
decltype(cj) y = x;  // y为const int&。
decltype(cj) z;  // 错误，z是一个引用，必须被初始化。

int i = 42, *p = &i, &r = i;
decltype(r +0) b;  // b为int。
decltype(*p) c;  // 错误，c为int&，必须被初始化。

decltype((i)) d;  // 错误，d为int&，必须被初始化。
```

## 类型转换

类型转换将一种类型转换为另一种类型。类型转换运算符（包括```dynamic_cast```）不会修改运算对象，而是返回转换后的值。

### 隐式转换

**隐式转换（implicit conversion）**在表达式中自动完成。在以下情况下会发生隐式转换：

- 在大多数表达式中，比```int```小的整型会被提升为至少和```int```一样大的整型。这被称为整型提升。
- 在条件表达式中，非布尔值转换为布尔值。
- 初始化过程中，初始值转换为变量的类型；赋值语句中，右侧运算对象转换为左侧运算对象的类型。
- 算术运算或关系运算的运算对象会转换为同一种类型。
- 函数调用实参到形参的类型转换。

#### 算术转换

**算术转换（arithmetic conversion）**将一种算术类型转换为另一种算术类型，通常算术转换会将一个运算符的运算对象转换为最宽的运算对象类型。对于整型，会转换为运算对象中最大的整型；如果运算对象既有整型又有浮点型，则整型转换为浮点型；对于浮点型，```long double```类型最宽，其次是```double```，最后是```float```。

通常，整型算术转换首先执行**整型提升（integral promotion）**，然后执行其他转换。

##### 整型提升

整型提升负责将小整型转换为大整型。

对于```bool```、```char```、```short```的有符号与无符号类型，如果它们的值能存放在```int```中，则转换为```int```，否则转换为```unsigned int```。

较大的字符类型会提升为```int```、```unsigned int```、```long```、```unsigned long```、```long long```、```unsigned long long```中能容纳下该值的最小类型。

##### 符号转换

如果一个运算对象为无符号类型，另一个运算对象为有符号类型，如果无符号类型不小于有符号类型，则有符号类型的运算对象转换为该无符号类型；否则，如果无符号类型的取值被有符号类型的取值包含，则无符号类型的运算对象转换为该有符号类型，否则有符号类型的运算对象转换为该无符号类型。

##### 取值

- 当把非布尔类型的算术值赋值给```bool```对象时，算术值为0则对象取值```false```，否则取值```true```。
- 当把布尔值赋给非布尔类型的算术变量时，布尔值为```false```则对象取值0，否则取值1。
- 当把浮点数赋给整数对象时，结果值为浮点数的整数部分。
- 当把整数赋给浮点类型时，小数部分记为0；可能会有精度损失。
- 当赋给无符号类型一个超出其表示范围内的值，结果是对该值对无符号类型可表示的总数取模后的余数。
- 当赋给有符号类型一个超出其表示范围内的值，结果是**未定义的（undefined）**，此时程序有可能继续工作、崩溃或产生垃圾数据。

不要混用有符号类型与无符号类型。

```c++
unsigned u = 10;
int i = -42;
cout << i + i << endl;  // 输出-84。
cout << u + i << endl;  // 如果int占32位，则输出4294967264。
```

```c++
unsigned u1 = 42, u2 = 10;
cout << u1 - u2 << endl;  // 输出32。
cout << u2 - u1 << endl;  // 正确，但输出取模后的值。
```

```c++
for (unsigned u = 10; u >= 0; --u) {  // 错误：u永远不会小于0，导致无限循环。
    cout << u << endl;
}

// 将错误的for循环改写为正确的while循环。
unsigned u = 11;
while (u > 0) {
    --u;
    cout << u << endl;
}
```

#### 其他隐式转换

- 在大多数表达式中，数组自动转换为指向数组首元素的指针。
- 指向任意非常量的指针可转换为```void*```，指向任意对象的指针可转换为```const void*```。
- 指针与算术类型可转换为布尔类型。如果指针为空或算术类型的值为0，转换为```false```，否则转换为```true```。
- 顶层```const```与底层```const```规定的转换。
- 类类型定义的转换。

### 显式转换

显式转换需要程序员强制将一种类型转换为另一种类型（请求一个显式转换被称为**cast**）。

```c++
int i = 8, j = 5;
double slope = i / j;  // slope = 1；如果想要得到1.6，需要强制转换。
```

强制类型转换干扰了正常的类型检查，因此应谨慎使用之。

#### 命名的强制转换

命名的强制转换具有形式：```cast_name<type>(expr)```。其中，```cast_name```为```static_cast```、```dynamic_cast```、```const_cast```或```reinterpret_cast```。```expr```为被转换的对象，```type```为要求的类型。如果```type```为引用，则结果为左值。

##### ```static_cast```

任何明确定义（well-defined）的类型转换，只要转换不涉及底层```const```（但是可以绕过底层```const```），都可以使用```static_cast```。

```c++
double slope = static_cast<double>(j) / i;  // slope为0.625。
```

当需要将较大的算术类型转换为较小的算术类型时，```static_cast```很有用，此时编译器不会给出精度损失的警告信息。

```static_cast```对于编译器无法自动执行的类型转换也很有用。

```c++
void *p = &d;
double *dp = static_cast<double*>(p);  // 
```

##### ```const_cast```

```const_cast```只能用于改变对象的底层```const```或底层```volatile```，并且在命名的强制转换中，只有```const_cast```可以去除对象的底层```const```或底层```volatile```。

如果对象本身就包含底层```const```或底层```volatile```并且去掉其该底层```const```或底层```volatile```，则通过该对象修改关联的底层```const```或底层```volatile```变量的值会产生未定义行为。

```C++
const char *pc;
char *p = const_cast<char*>(pc);  // 通过p对pc指向的对象写值是未定义的
```

```c++
const char *cp;
static_cast<char*>(cp);  // 错误，static_cast不能去掉底层const。
static_cast<const int*>(cp);  // 错误，static_cast不能改变底层const。
static_cast<string>(cp);  // 正确，const char*可以转换为string，绕过了底层const。
const_cast<string>(cp);  // 错误，const_cast只能用于改变cp的底层const。
```

```const_cast```一般用于重载函数中，在其他情况下使用```const_cast```通常意味着设计缺陷。

```c++
const string &shorterString(const string &s1, const string &s2) {
    return s1.size() <= s2.size() ? s1 : s2;
}

string &shorterString(string &s1, string &s2) {
    auto &r = shorterString(const_cast<const string&>(s1), const_cast<const string&>(s2));  // 这里去掉底层const只是为了调用重载函数。
    return const_cast<string&>(r);  // 由于r实际上绑定到了非常量引用上，这里去掉r的底层const属性是安全的。
}
```

##### ```reinterpret_cast```

```reinterpret_cast```用于对对象的位模式提供低层次的解释。

```c++
int *ip;
char *pc = reinterpret_cast<char*>(ip);
```

```reinterpret_cast```是非常危险的，关键在于其将内存中的类型信息重新解释了，但是编译器不会给出任何警告或错误提示的信息。```reinterpret_cast```的结果依赖于机器。

#### 旧式的强制转换

在早期C++语言中，包括两种形式的强制类型转换：```type(expr)```与```(type)expr```。其中```expr```为被转换的对象，```type```为要求的类型。第一种为函数风格的转换，第二种为C语言风格的转换。

## 运行时类型识别

**运行时类型识别（run-time type identification, RTTI）**通过两个运算符实现：

- ```typeid```运算符：用于返回表达式的类型。
- ```dynamic_cast```：用于将基类指针或引用安全地转换为派生类的指针或引用。

### ```dynamic_cast```运算符（dynamic_cast operator）

```dynamic_cast```的使用形式有三种：

```c++
dynamic_cast<type*>(e)
dynamic_cast<type&>(e)
dynamic_cast<type&&>(e)
```

其中，```type```为类类型，通常包含虚函数。在第一种形式中，```e```必须是有效的指针；在第二种形式中，```e```必须是左值；在第三种形式中，```e```不能是左值。如果```e```是以下三种类型之一，则转换成功：```e```：```e```是```type```的公有派生类；```e```是```type```的公有基类；```e```就是```type```类型。否则，转换失败。如果转换目标是指针类型且转换失败，则结果为```0```；如果转换目标为引用类型且转换失败，则抛出```bad_cast```异常。可以对空指针执行```dynamic_cast```，此时结果为对应类型的空指针。

```c++
// Derived为Base的派生类。
if (Derived *dp = dynamic_cast<Derived*>(bp)) {  // 返回非0指针。
    // 使用dp指向的Derived对象。
} else {  // 返回空指针。
    // 使用bp指向的Base对象。
}
```

```c++
void f(const Base &b) {
    try {
        const Derived &d = dynamic_cast<const Derived&>(b);
        // 使用b引用的Derived对象。
    } catch (bad_cast) {
        // 处理类型转换失败的情况。
    }
}
```

### ```typeid```运算符（typeid operator）

```typeid```接受一个参数，参数可以是任意表达式或类型名，```typeid```返回一个常量对象的引用，该对象的类型为标准库类型```type_info```或```type_info```的公有派生类型。返回值表示参数的类型。

```typeid```忽略顶层```cosnt```；如果表达式为引用，```typeid```返回引用所引对象的类型；当```typeid```作用于数组或函数时，直接返回数组或函数的类型而不会执行向指针的转换。

如果运算对象不属于类类型，或是一个不包含任何虚函数的类时，```typeid```运算符指示的是运算对象的静态类型；如果运算对象为至少定义了一个虚函数的类的左值时，则其类型在运行时求得。

```typeid```是否需要运行时检查决定了其是否会对表达式求值。只有当类型函数虚函数时，编译器才会对表达式求值。如果表达式的动态类型可能与静态类型不同，则表达式必须运行时求值以确定结果类型。当使用```typeid(*p）```时，如果运算对象指针```p```所指的类型不含有虚函数，则```p```可以不是有效指针，否则```p```必须是有效指针；如果```p```是空指针，则抛出```bad_typeid```异常。

通常，我们使用```typeid```比较两条表达式的类型是否相同，或一条表达式的类型与指定类型是否相同。

```c++
Derived *dp = new Derived;
Base *bp = dp;

if (typeid(*bp) == typeid(*dp)) {
    // bp与dp指向同一类型对象。
}

if (typeid(*bp) == typeid(Derived)) {
    // bp实际指向Derived对象。
}

if (typeid(bp) == typeid(Derived)) {
    // 此处的代码永远不会执行。
    // 因为bp为指针，返回其静态类型。
}
```

#### ```type_info```类

```type_info```类的精确定义随编译器不同而不同。但是其保证定义在头文件```typeinfo```中，并至少提供如下操作，其中```t```、```t1```与```t2```为```typeinfo```对象：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>t1 == t2</td>
        <td>如果t1与t2表示相同类型，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>t1 != t2</td>
        <td>如果t1与t2表示不同类型，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>t.name()</td>
        <td>返回C风格字符串，表示类型名的可打印形式（printable version），类型名的生成方式取决于系统，并且不要求与程序中使用的类型名一致。对于name的返回值唯一确定的是，类型不同则返回的字符串有所区别。</td>
    </tr>
    <tr>
        <td>t1.before(t2)</td>
        <td>返回一个bool值，表示t1是否位于t2前。before所采用的的顺序是依赖于编译器的。</td>
    </tr>
</table>

因为```type_info```类旨在用作基类，因此其提供了```public```虚析构函数。当编译器希望提供额外的类型信息时，通常在```type_info```的派生类中完成。

```type_info```没有默认构造函数，拷贝构造函数、移动构造函数与赋值运算符都被定义成删除的。因此无法定义或拷贝```type_info```类型的对象，也不能为其赋值。创建```type_info```对象的唯一方式是使用```typeid```运算符。

```c++
int arr[10];
Derived d;
Base *p = &d;
cout << typeid(42).name() << ", "
     << typeid(arr).name() << ", "
     << typeid(Sales_data).name() << ", "
     << typeid(string).name() << ", "
     << typeid(p).name() << ", "
     << typeid(*p).name() << endl;
// 一种可能的输出是：i, A10_i, 10Sales_data, Ss, P4Base, 7Derived
```

```c++
// 使用RTTI为具有继承关系的类实现相等运算符。
// 不能定义一套虚函数，令其在继承的各个层次上分别执行相等判断操作（定义一个相等运算符作用于于基类的引用，该运算符将其工作委托给虚函数equal，equal负责实际操作），因为equal的形参需要接受基类的引用，其将只能使用基类成员。

class Base { 
    friend bool operator==(const Base&, const Base&);
public:
    // 接口成员。
protected:
    virtual bool equal(const Base&) const;
    // 数据与其他用于实现的成员。
};
class Derived: public Base {
public:
    // 其他接口成员。
protected:
    bool equal(const Base&) const;
    // 数据与其他用于实现的成员。
};

bool operator==(const Base &lhs, const Base &rhs) {
    return typeid(lhs) == typeid(rhs) && lhs.equal(rhs);
}

bool Derived::equal(const Base &rhs) const {
    auto r = dynamic_cast<const Derived&>(rhs);
    // 比较两个Derived对象并返回结果。
}

bool Base::equal(const Base &rhs) const {
    // 比较两个Base对象并返回结果。
}
```

## 类型转换模板

**类型转换（type transformation）模板**定义在头文件```type_traits```（这个头文件中的类通常用于模板元编程（template metaprogramming）中。

标准类型转换模板如下，其中```Mod```表示类型转换模板，```T```为模板类型形参：

<table>
    <tr>
        <th>Mod</th>
        <th>T</th>
        <th>Mode&lt;T&gt;::type</th>
    </tr>
    <tr>
        <td rowspan="2">remove_reference</td>
        <td>X&amp;或X&amp;&amp;</td>
        <td>X</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
    <tr>
        <td rowspan="2">add_const</td>
        <td>X&amp;、const X或函数</td>
        <td>T</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>const T</td>
    </tr>
    <tr>
        <td rowspan="3">add_lvalue_reference</td>
        <td>X&amp;</td>
        <td>T</td>
    </tr>
    <tr>
        <td>X&amp;&amp;</td>
        <td>X&amp;</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T&amp;</td>
    </tr>
    <tr>
        <td rowspan="2">add_rvalue_reference</td>
        <td>X&amp;或X&amp;&amp;</td>
        <td>T</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T&amp;&amp;</td>
    </tr>
    <tr>
        <td rowspan="2">remove_pointer</td>
        <td>X*</td>
        <td>X</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
    <tr>
        <td rowspan="2">add_pointer</td>
        <td>X&amp;或X&amp;&amp;</td>
        <td>X*</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T*</td>
    </tr>
    <tr>
        <td rowspan="2">make_signed</td>
        <td>unsigned X</td>
        <td>X</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
    <tr>
        <td rowspan="2">make_unsigned</td>
        <td>有符号类型</td>
        <td>unsigned T</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
    <tr>
        <td rowspan="2">remove_extent</td>
        <td>X[n]</td>
        <td>X</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
    <tr>
        <td rowspan="2">remove_all_extents</td>
        <td>X[n1][n2]...</td>
        <td>X</td>
    </tr>
    <tr>
        <td>其他</td>
        <td>T</td>
    </tr>
</table>

```c++
template <typename It> auto fcn2(It beg, It end) -> typename remove_reference<decltype(*beg)>::type {  // 返回元素的值而非引用。
    // ...
    return *beg;
}
```

# 变量

变量（variable）提供一个具名的、可供程序操作的存储空间。C++中每个变量都有数据类型，数据类型决定变量所占内存空间的大小、内存布局方式、值的范围与可参与的运算。在C++中，变量与对象（object）可互换使用。

## 标识符

标识符（identifier）用于标识一个变量或其他实体（包括函数、类、类型别名等等），由字母、数字与```_```组成，且必须以字母或```_```开始。标识符没有长度限制，且对大小写敏感。

用户自定义的标识符不能出现```__```，不能以```_```紧跟大写字母开头，定义在函数体外的标识符不能以```_```开头。

C++保留了一些名字供语言本身使用，这些名字不能被用作标识符，包括关键字与操作符替代名。

关键字如下：

```c++
alignas, alignof, asm, auto, bool, break, case, catch, char, char16_t, char32_t, class, const, constexpr, const_cast, continue, decltype, default, delete, do, double, dynamic_cast, else, enum, explicit, export, extern, false, float, for, friend, goto, if, inline, int, long, mutable, namespace, new, noexcept, nullptr, operator, private, protected, public, register, reinterpret_cast, return, short, signed, sizeof, static, static_assert, static_cast, struct, switch, template, this, thread_local, throw, true, try, typedef, typeid, typename, union, unsigned, using, virtual, void, volatile, wchar_t, while
```

操作符替代名如下：

```c++
and, bitand, compl, not_eq, or_eq, xor_eq, and_eq, bitor, not, or, xor
```

标识符的命名要体现实际含义。变量名一般用小写字母表示，尤其要以小写字母开头；用户自定义的类名一般以大写字母开头；多个单词组成的标识符单词之间要有区分，一般用大写或```_```区分，如```student_loan```或```studentLoad```。

## 变量的定义

变量定义（definition）由一个**基本类型（base type）**（包括限定符，如```const```或```constexpr```）与紧跟其后的**声明符（declarator）**列表组成。每个声明符包含若干类型修饰符（如```&```或```*```）与一个变量名（标识符），其命名了一个变量并指定该变量为与基本数型相关的某种类型。变量定义同时指定了变量的类型（除去变量名的部分）。

```c++
int *p1, p2; // p1是指向int的指针，p2是int。
int* p1, p2; // p1是指向int的指针，p2是int，类型修饰符属于p1。
```

## 变量的声明

变量**声明（declaration）**使得变量的名字为程序所知并可使用。变量声明与变量定义的结构类似，只是需要在定义形式中前置```extern```，并且不能显式初始化变量，否则其就变成了定义。在函数体内部，试图初始化一个由```extern```标记的变量将引发错误。

变量定义本身也是对变量的声明。

对变量的声明可以任意多次，但是定义不能重复。

通常建议在头文件中声明变量，在源文件中定义变量。

## 变量的初始化

当对象在创建时获得特定的值，则称这个对象被**初始化（initialized）**了。注意初始化不是赋值，赋值指使用一个新的值替代对象当前值。

在同一条语句中，可以使用先定义的变量初始化后定义的变量：

```c++
double price = 109.99, discount = price * 0.16;
double salePrice = applyDiscount(price, discount)
```

初始化可以使用等号（```=```）、花括号包围值（```{}```）、括号包围值（```()```）等形式，其中等号可以其他两种方式结合。

```c++
int units_sold = 0;
int units_sold = {0};
int units_sold{0};
int units_sold(0);
```

使用花括号包围值进行初始化的方式被称为**列表初始化（list initialization）**。列表初始化不允许窄化转换，即被初始化对象的类型的取值范围必须包含用于初始化的对象的类型的取值范围。

```c++
long double ld = 3.1415926536;
int a(ld), b = {ld};  // 错误，从ld（long double）到b（int）存在丢失信息的风险
int c(ld), d= {ld};
```

使用拷贝初始化时，只能提供一个初始值；当提供类内初始值是，只能使用拷贝初始化或```{}```初始化，不能使用```()```初始化；如果提供元素初始值列表，则只能将初始值放在花括号（不能是圆括号）里进行列表初始化。

```c++
vector<string> v1{"a", "an", "the"};  // 列表初始化。
vector<string> v2("a", "an", "the");  // 错误。
```

### 默认初始化

若定义变量时没有显式指定初始值，则变量被**默认初始化（default initialization）**。

对于内置类型的变量，若变量被定义于任何函数体外，则被初始化为```0```（对于指针，就是```nullptr```；对于```char```，就是```'\0'```；对于```bool```，就是```false```）；若（非静态）变量定义在函数体中，则其**不被初始化（uninitialized）**，其值未定义。

每个类决定其初始化对象的方式。

变量在以下情况下被默认初始化：

- 块作用域内不使用任何初始值定义一个非静态变量。
- 类本身含有使用合成的默认构造函数的类类型的成员。
- 类类型的成员没有在构造函数初始值列表中被显式初始化（且未使用类内初始值）。

值初始化会为变量提供一个默认初始值。变量在以下情况下被值初始化：

- 数组初始化过程中提供的初始值少于数组的大小。
- 不使用初始值定义一个局部静态变量。
- 通过书写形如```T()```的表达式显式请求值初始化，其中T为类型名（如```vector<int>```）。

每个类决定该类的对象被如何初始化。

### 值初始化

如果变量被**值初始化（value-initialized）**，则其会被赋予一个默认值。对于内置类型，该默认值一般为0；类类型对象的值初始化由类的默认构造函数决定。

## 作用域

**作用域（scope）**是程序的一部分，在其中的名字有特定的含义（且都会指向一个特定的实体，如变量、函数、类型等）。在C++中，大多数作用域以花括号定界。

定义于所有花括号外的名字一般拥有**全局作用域（global scope）**，一旦声明后，全局作用域的名字在整个程序的范围内都可使用。定义在花括号中的名字一般拥有**块作用域（block scope）**，一旦出了该块，这些名字就不可用。

一个作用域能包含其他作用域。被包含/嵌套的作用域被称为**内层作用域（inner scope）**，包含其他作用域的作用域被称为**外层作用域（outer scope）**。外层作用域中的名字一经声明，可以被内层作用域访问。

内层作用域可以重定义外层作用域中的名字（这两个名字可能指向不同的实体），此时如果内层作用域中的名字**正在作用域内（in scope）**，即被声明，则内层作用域中的名字会覆盖外层作用域中的名字，否则外层作用域中的名字仍然可见。

```c++
int reused = 42;  // reused拥有全局作用域。
int main() {
    int unique = 0;  // unique拥有局部作用域。
    cout << reused << " " << unique << endl;  // 输出：42 0。
    int reused = 0;  // 
    cout << reused << " " << unique << endl;
    cout << ::reused << " " << unique << endl;
    return 0;
}
```

定义在函数（包括形参与函数体）等局部作用域内的变量被称为**局部变量（local variable）**，这些变量可以隐藏外层作用域中声明的同名变量。

在C++中，基本的**名字查找（name lookup）**（寻找与所用名字匹配的声明）过程如下：

- 首先在名字所在的块中寻找其声明语句，只考虑在名字的使用之前出现的声明。
- 如果没找到，则查找外层作用域。
- 如果最终没有找到声明，则程序报错。

名字查找发生在类型检查前。

一般来说，在对象第一次被使用的地方定义它是一种好选择，因为这样做容易找到变量的定义，并更容易给它赋一个有用的初始值。如果一个函数可能用到某个全局变量，则几乎肯定不宜再定义一个同名的局部变量。

### 自动对象与局部静态对象

只存在于块执行期间的对象被称为**自动对象（automatic object）**，这种对象拥有块生命周期（lifetime）。它们在控制流路径经过变量定义时被创建，当控制流到达定义所在的块末尾时被销毁。当块执行结束时，在块中创建的自动对象的值就变成未定义的了。

**局部静态对象（local static object）**则是在执行路径第一次经过变量定义语句时被初始化，直到程序终止才被销毁。

```c++
// 统计自身被调用多少次的函数。
// 仅用于演示局部静态对象的功能。
size_t count_calls() {
    static size_t ctr = 0;  // value will persist across calls。
    return ++ctr;
}
int main() {
    for (size_t i = 0; i != 10; ++i) {
        cout << count_calls() << endl;  // 输出从1到10的数字。
    }
    return 0;
}
```

如果局部静态对象没有被显式初始化，则被值初始化。

# 表达式

**表达式（expression）**由一个或多个**运算对象（operand）**组成，对表达式求值得到**结果（result）**。字面值与变量是最简单的表达式。通过**运算符（operator）**将相应数量的运算对象结合起来可以得到更复杂的表达式。其中，**复合表达式（compound expression）**指含有至少两个运算符的表达式。

C++定义了一元运算符（unary operator）、二元运算符（binary operator）与三元运算符（ternary operator）。所谓元，就是某个运算符接受的运算对象的数量。函数调用也是一种特殊的运算符，其对运算对象的数量没有限制。所有内置的二元运算符两边各接受一个参数。

运算符的优先级（precedence）决定运算对象与运算符结合的紧密程度，运算对象总是与优先级更高的运算符结合在一起。括号无视优先级顺序，即括号内的子表达式一定结合在一起。

运算符的结合律（associativity）决定同优先级的运算符的组合方式。

除了某些特殊的运算符，绝大多数运算符没有规定求值顺序（order of evaluation）。有4种内置的运算符规定了求值顺序：逻辑与（```&&```）、逻辑或（```||```）、条件运算符（```? :```）与逗号运算符（```,```）。

```c++
int i = f1() * f2();  // i最后被求值，f1()与f2()先于“*”求值，f1()与f2()的求值顺序不定。

int i = 0;
cout << i << " " << ++i << endl;  // 行为未定义，如果先求值i再求值++i则输出“0 1”，否则输出“1 1”。
```

如果改变了某个运算对象的值，则不要在同一个表达式的其他地方使用这个运算对象，除非当改变运算对象的子表达式本身就是另外一个子表达式的运算对象，如```*++iter```。

C++表达式要么是**左值（lvalue）**，要么是**右值（rvalue）**。当一个对象被用作左值时，用的对象的身份（identity），即内存中的位置；当一个对象被用作右值时，用的是对象的值（value）或内容（content）。

右值不能出现在赋值运算符的左边。除了右值引用，需要用到右值的地方可以用左值代替，需要用到左值的地方未必能用右值代替。

所有的变量表达式是左值。

运算符的优先级排序如下：

1. 作用域运算符（```::```）。
2. 点运算符（```.```）、箭头运算符（```->```）、下标运算符（```[]```）、函数调用运算符（```()```）、函数风格的类型转换运算符（```()```）。
3. 后置递增运算符（```++```）、后置递减运算符（--）、typeid（参数为对象或类型）、4种命令的强制转换运算符（```static_cast```、```const_cast```、```reinterpret_cast```与```dynamic_cast```）。
4. 前置递增运算符（```++```）、前置递减运算符（```--```）、位求反运算符（```~```）、逻辑非运算符（```!```）、负号（```-```）、正号（```+```）、解引用运算符（```*```）、取地址符（```&```）、C语言风格的类型转换运算符（```()```）、```sizeof```（参数为对象或类型）、```sizeof...```、```new```、```new[]```、```delete```、```delete[]```、```noexcept```。
5. ```->*```、```.*```。
6. 乘号（```*```）、除号（```/```）、取模（```%```）运算符。
7. 加号（```+```）、减号（```-```）。
8. 左移运算符（```<<```）、右移运算符（```>>```）。
9. 小于（```<```）、小于等于（```<=```）、大于（```>```）、大于等于（```>=```）。
10. 等于（```=```）、不等于（```!=```）。
11. 位与（```&```）。
12. 位异或（```^```）。
13. 位或（```|```）。
14. 逻辑与（```&&```）。
15. 逻辑或（```||```）。
16. 条件运算符（```? :```）。
17. 赋值运算符（```=```）。
18. 复合赋值运算符。
19. ```throw```。
20. 逗号运算符（```,```）。

## 算术运算符 

算术运算符（arithmetic operators）包括正号（```+```）、负号（```-```）、乘号（```*```）、除号（```/```）、加号（```+```）与减号（```-```）与取模运算符（```%```）。

除非特殊说明，其运算规则与数学上对应的算术运算符的运算规则一致。除非特殊说明，算术运算符能作用于任意算术类型。算术运算符的运算结果可能会溢出。算术运算符左结合，其运算对象与求值结果均为右值。

正号、加号与减号能作用于指针。当正号作用于指针或算术值时，返回对象值的（可能提升后的）副本。负号运算符作用于算术值返回对象值的（可能提升后的）副本取负后的结果。

```c++
bool b = true;
bool b2 = -b;  // 返回true，因为b先被提升为int。
```

当算术运算符作用于整数时，得到的结果（如果合法的话）仍然是整数。整数相除的结果向0取整。对于任意整数$m$与$n\neq 0$，满足$m/n*n+m%n=m$。因此取模运算的结果如果不等于0，则与被除数同号。

## 关系运算符

关系运算符（relational operator）包括小于（```<```）、小于等于(```<=```）、大于（```>```）、大于等于（```>=```）、等于（```=```）与不等于（```!=```）。除非特殊说明，其运算规则与数学上对应的算术运算符的运算规则一致。关系运算符返回布尔值指示比较结果是否正确。关系运算符左结合，其运算对象与求值结果均为右值。

```c++
if (i < j < k)  // 注意i < j返回false或true，随后转换为0或1，再与k比较。因此若k > 1则永远返回true。因此关系运算符的连写与数学意义上的连写含义不同。
```

## 逻辑运算符

逻辑运算符（logical operator）包括逻辑非运算符（```!```）、逻辑与运算符（```&&```）与逻辑或运算符（```||```）。其中```!```为一元运算符（位于运算对象的左边），```&&```与```||```为二元运算符。逻辑运算符左结合，其运算对象与求值结果均为右值。

```!```将运算对象的真值（```true```或```false```）取反后返回，逻辑上运算对象被转换为布尔值，然后真值被取反。应尽量避免使用与```true```或```false```相等性比较来测试运算对象的真值。

```c++
if (!val) {  // val为int，测试其是否不等于0。
    // ...
}
```

```c++
if (val == true)  {  // 假设val为int，true被转换为1，只有当val等于1才返回true。
    // ...
}
```

如果```&&```的两个运算对象都为```true```，则返回```true```，否则返回```false```。

如果```||```的两个对象都为```false```，则返回```false```，否则返回```true```。

```&&```与```||```都是先求左侧对象的值再求右侧对象的值。如果```&&```的左侧运算对象求值为```false```，则其不会对右边的运算对象求值而是直接返回```false```；如果```&&```的左侧运算对象求值为```true```，则其不会对右边的运算对象求值而是直接返回```true```。这被称为**短路求值（short-circuit evaluation）**。

```c++
for (const auto &s : text) {  // 假设text为vector<string>。
    cout << s;
    if (s.empty() || s[s.size() - 1] == '.') {  // 如果s为空，直接返回true，避免求值第二个式子导致异常。
        cout << endl;
    } else {
        cout << " ";
    }
}
```

## 赋值运算符

赋值运算符（ assignment operator）为二元运算符，右结合，其左侧必须是一个可修改的左值。

注意赋值与初始化是两个概念。

与列表初始化类似，允许用花括号包围的初始值列表作为赋值运算符的右侧运算对象，此时不允许窄化转换。

赋值运算符的优先级很低，需要在适当时候加括号。

```c++
int i;
while ((i = get_value()) != 42) {
    // ...
}
```

### 复合赋值运算符

二元的算术运算符与位运算符都有对应的复合赋值运算符（compound assignment operator），形如“op=”，其中op为算术运算符或位运算符。对于运算对象```a```与```b```，```a op= b```等价于```a = a op b```。唯一的区别是前者对左侧运算对象求值一次（对```a```原地求值而不用赋值）而后者对左侧运算对象求值两次。

## 递增与递减运算符

递增运算符（increment operator）（```++```）与递减运算符（decrement operator）（```--```）作用于左值，均有两种形式：前置版本与后置版本。前置版本的运算符作用于运算对象的左边，对该对象加1或减1后返回该对象（左值）；后置版本的运算符作用于运算对象的右边，对该对象加1或减1，返回对象原来的值（右值）。

尽量避免使用后置版本的递增与递减运算符，因为后置版本需要将原始值存下来以便返回，带来开销，如果原始值不需要，则后置版本的操作就是一种浪费。

如果后置递增（或递减，相对少见）运算符作用于迭代器```p```且用于遍历元素，则其经常与解引用运算符连用，形如：```*p++```（或```*p--```）。

```c++
auto pbeg = v.begin();  // v为vector。
while (pbeg != v.end() && *beg >= 0) {
    cout << *pbeg++ << endl;  // 输出当前值并将pbeg向前移动一个元素。
}
```

如果在表达式中对某个对象使用递增或递减运算符，并且在该表达式的其他位置也出现这个对象，则行为可能未定义。

```c++
while (beg != s.end() && !isspace(*beg)) {  // s为string。
    /* 赋值语句的结果未定义。
     * 如果赋值语句左侧先被执行，则等价于：*beg = toupper(*beg);
     * 如果赋值语句右侧先被执行，则等价于：*(beg + 1) = toupper(*beg);
     * 甚至可能存在其他求值方式。
     */
    *beg = toupper(*beg++);
}
```

## 成员访问运算符

成员访问运算符（the member access operator）包括点运算符（```.```）与箭头运算符（```->```）。点运算符用于获取类对象```obj```的一个成员```mem```，形式为：```obj.mem```；若```ptr```为一个指向类对象的指针，则```ptr->mem```等价于```(*ptr).mem```。如果点运算符的成员所属的对象为左值，则其返回左值，否则返回右值；箭头运算符返回左值。成员访问运算符右结合。

## 条件运算符

条件运算符（the conditional operator）（**?: operator**）是唯一内置的三元运算符，使用方式为：```cond ? expr1 : expr2```。其中```cond```为用于条件的表达式，```expr1```与```expr2```为两个类型相同或可以转换为同一类型的表达式。条件运算符首先对```cond```求值，如果其为```true```则求值```expr1```并返回该值，否则求值```expr2```并返回该值。当```expr1```与```expr2```均为左值或转换为同一种左值类型时，运算结果为左值，否则运算结果为右值。条件运算符右结合。

```c++
string finalgrade = (grade > 90) ? "high pass" : (grade < 60) ? "fail" : "pass";  //假设grade为int，靠右边的条件运算构成了靠左边的条件运算的:分支。
```

条目运算符的优先级很低，需要在适当时候加括号。

```c++
// grade为int。
cout << ((grade < 60) ? "fail" : "pass");  // 输出fail或pass。
cout << (grade < 60) ? "fail" : "pass";  // 输出0或1。
cout << grade < 60 ? "fail" : "pass";  // 错误，试图比较cout与60。
```

## 位运算符

位运算符（the bitwise operator）的运算对象为整型对象，并将对象看做是二进制位的集合。包括位求反运算符（```~```）、左移运算符（```<<```）、右移运算符（```>>```）、位与运算符（```&```）、位异或运算符（```^```）与位或运算符（```|```）。其中```~```为一元运算符（位于运算对象的左边），其他运算符为二元运算符。位运算符左结合，其运算对象与求值结果均为右值。位运算符不会修改运算对象，而是返回运算结果。

位运算符对于符号位的处理取决于具体实现，因此最好将位运算符作用于无符号类型。

位求反运算符将运算对象逐位求反（```0```变成```1```，```1```变成```0```）。

左移运算：```bits << n```将整型```bits```的所有位向左移动$n$位（丢弃掉这$n$位），并在低位填充$n$位```0```。其中$n$必须为非负数且严格小于结果的位数，否则结果未定义。

右移运算：```bits >> n```将整型```bits```的所有位向右移动$n$位（丢弃掉这$n$位），并在高位填充$n$位。其中$n$必须为非负数且严格小于结果的位数，否则结果未定义。如果```bits```是（或转换为）无符号数，则填充的$n$位全```0```，否则填充的$n$位全为```0```或全为符号位的副本，取决于具体实现。

```&```、```^```与```|```对两个运算对象逐位进行相应的逻辑操作。

```c++
// 使用位运算符表示测验结果，假设一共30个学生。

unsigned long quiz1 = 0;  // quiz1的前30位的每一位表示一个学生。0表示未通过测验，1表示通过测验。初始时，全部位为0。
quiz1 |= 1UL << 27;  // 表示学生No.27通过了测验。
quiz1 &= ~(1UL << 27);  // 表示学生No.27没有通过测验。
bool status = quiz1 & (1UL << 27);  // 查看学生No.27是否通过测验。
```

## ```sizeof```

```sizeof```运算符返回一个表达式或一个类型名字所占的字节数，是一个```size_t```类型的常量表达式。```sizeof```右结合，使用方式为：```sizeof(type)```或```sizeof expr```。其中```type```为类型，```expr```为表达式。

```sizeof```不会计算运算对象的值，因此即使表达式非法也没关系。

```c++
Sales_data data, *p;  // Sales_data为一个类。
sizeof(Sales_data);  // 返回Sales_data类型的对象所占空间的大小。
sizeof data;  // 返回data对象所占空间大小。
sizeof p;  // 返回指针所占空间大小。
sizeof *p;  // 返回指针指向的对象所占空间大小，即sizeof(Sales_data)。因为sizeof右结合，所以*p作为sizeof的求值对象。p不需要合法。
sizeof data.revenue;  // 返回Sales_data的revenue静态成员所占空间大小。
sizeof Sales_data::revenue;  // 等价于：sizeof data.revenue。
```

```sizeof```运算符的结果部分取决于其作用的类型：

- 对```char```或类型为```char```的表达式执行```sizeof```运算得到```1```。
- 对引用类型求值得到引用绑定的对象所占空间的大小。
- 对指针执行```sizeof```运算得到指针所占空间的大小。
- 对解引用指针执行```sizeof```运算得到指针指向的对象所占空间的大小，指针不必有效。
- 对数组执行```sizeof```运算返回整个数组所占空间的大小，即对数组中每个元素执行```sizeof```运算并求和。注意```sizeof```不会将数组转换为指针。
- 对```string```、```vector```以及其他标准库容器执行```sizeof```返回运算只返回该类型固定部分所占空间的大小，而不是元素占用的空间大小。

```c++
constexpr size_t sz = sizeof(ia) / sizeof(*ia);  // 假设ia为数组，返回ia的元素数量。
int arr2[sz];  // 正确，因为sz为常量表达式。
```

## 逗号运算符

逗号运算符（comma operator）（```,```）为二元运算符，左结合。逗号运算符先对左侧表达式求值，然后将求值结果丢弃，再对右侧表达式求值，其求值结果为右侧表达式的值。如果右侧表达式为左值，则求值结果为左值，否则为右值。

逗号运算符经常用在```for```循环中。

```c++
vector<int>::size_type cnt = ivec.size();  // 假设ivec为vector<int>。
for (vector<int>::size_type ix = 0; ix != ivec.size(); ++ix, --cnt)
    ivec[ix] = cnt;
```

```c++
// 仅用于演示，不要这么写代码
int i = 15;
int j = (++i, ++i);  // 必须要加括号，因为逗号运算符优先级最低。否则错误，因为初始化语句不是表达式。
cout << j << endl;
```

# 语句

## 简单语句

以分号结束的一条语句（statement）为简单语句。在一个表达式的末尾加上一个分号就变成了**表达式语句（expression statement）**。表达式语句执行表达式并丢弃求值结果。

如果一条语句只包含分号，则其为**空语句（null statement）**。

```c++
while (cin >> s && s != sought);  // s与sought为string。

int ival = v1 + v2;;  // v1与v2为int，第二个分号表示多余的空语句。

while (iter != svec.end());  // svec为vector<string>，iter为对应的迭代器。
	++iter;  // 出现多余的分号导致该语句不属于while语句的一部分。
```

**复合语句（compound statement）**，通常被称为**块（block）**，为花括号包围起来的（可能为空）的语句序列。复合语句可能包含多条语句，但是逻辑上视为一条语句。空的块等价于空语句。

一个块就是一个作用域。

## 条件语句

### ```if```语句

**```if```语句（```if``` statement）**有两种形式：

```c++
if (condition)
	statement
```

```c++
if (condition)
	statement1
else
	statement2
```

其中```condition```表示可转换为```bool```类型的表达式，```statement```、```statement1```与```statement2```为语句。

在第一种形式中，如果```condition```求值为```true```，则执行```statement```，否则跳过```statement```。在第二种形式中，如果```condition```求值为```true```，则执行```statement1```，否则执行```statement2```。

```if```与```else```内部可以有条件语句，此时```if```分支可能多于```else```分支，```else```与哪个```if```匹配的问题被称为**悬垂```else```（dangling ```else```）**问题。C++规定```else```与之前最近的、尚未```else```的```if```匹配。

```c++
// 根据成绩给出特定的成绩等级
vector<string> scores = {"F", "D", "C", "B", "A", "A++"};
int grade;
string lettergrade;
cin >> grade;
if (grade < 60)
	lettergrade = scores[0];
else  // 错误，缺少花括号
	lettergrade = scores[(grade - 50)/10];
 	if (grade != 100)
        // 如果成绩个位数大于7，添加一个“+”；如果成绩个位数小于3，添加一个“-”。
 		if (grade % 10 >= 3)  // 错误，需要添加花括号。
            if (grade % 10 > 7)
 				lettergrade += '+';
 		else
 			lettergrade += '-';
```

### ```switch```语句

**```switch```语句（```switch``` statement）**的形式为：

```c++
switch (expr) {
	case v_1:
		statement_1
	case v_2:
		statement_2
	...
	case v_n:
		statement_n
	default:
		default_statement
}
```

其中，```expr```为表达式，其求值结果必须为整型；```v_1```、```v_2```、……、```v_n```必须为互不相等的整型常量表达式，```case```与其对应的整型常量表达式构成**case标签（```case``` label）**；```default```被称为**```default```标签（```default``` label）**，是一种特殊的```case```标签；```statement_1```、```statement_2```、……、```statement_n```、```default_statement```为语句，可以为空（不是指空语句），但是最后一条语句不能为空（不一定是```default_statement```）。```default```标签及其相关的```default_statement```可以位于任意的```case```标签前后，也可以被省略。

该语句首先对```expr```求值，然后找到与之相等的```case```标签的值，之后执行该```case```标签后的所有语句（如果```default```标签位于其后，则```default_statement```也被执行）；如果没有```case```标签的值匹配```expr```且有```default```标签，则执行```default```标签后的所有语句（如果其后有```case```标签，则相关的语句也被执行）；如果没有```default```标签，则跳出```switch```语句，执行```switch```语句之后的语句。在以上过程中，如果遇到```switch```语句中的```break```语句，则跳出```switch```语句，执行```switch```语句之后的语句。

```c++
// 使用switch统计各元音字母在文本中出现的次数。
unsigned aCnt = 0, eCnt = 0, iCnt = 0, oCnt = 0, uCnt = 0;
char ch;
while (cin >> ch) {
    switch (ch) {
        case 'a':
            ++aCnt;
            break;  // 不要遗漏break。
        case 'e':
            ++eCnt;
            break;
        case 'i':
            ++iCnt;
            break;
        case 'o':
            ++oCnt;
            break;
        case 'u':
            ++uCnt;
            break;
    }
}
```

如果某个变量在```switch```语句内被初始化，并且程序可能不执行该初始化语句，但是可能在其他地方用到该变量，则报错（即不允许控制流将初始化的变量从作用域之外转移到作用域之内）。如果允许这种情况发生，那么可能使用了期望被初始化但未被初始化的变量。

> The answer is that it is illegal to jump from a place where a variable with an initializer is out of scope to a place where that variable is in scope:

```c++
// 如果想要程序正确，一是使用花括号包围case true标签后的语句，一是调转case true标签与case false标签（以及相关的语句）。在这两种情况下，case false标签对定义在其中的变量的使用非法。
switch (condition) {  // condition为一个布尔值。
    case true:
        string file_name;  // 错误，控制流绕过一个隐式初始化的变量。
        int ival = 0;  // 错误，控制流绕过一个显式初始化的变量。
        int jval;  // 正确，未初始化。
        break;
    case false:
        jval = next_num();
        if (file_name.empty()) {  // 不管有没有对file_name的使用，true标签对file_name初始化都是不正确的，关键是使用file_name的可能性总是存在的。
            // ...
        }
        // ...
}
```

## 迭代语句

**```while```语句（```while``` statement）**的形式为：

```c++
while (condition)
	statement
```

其中```condition```表示可转换为```bool```类型的表达式或初始化的变量声明（不能为空），```statement```为语句。程序不断迭代：判断```condition```是否为```true```，如果为```true```，执行```statement```；否则迭代结束，跳过```statement```执行下一语句。

定义在```condition```中的变量或```statement```中的变量每次迭代都经历从创建到销毁的过程，且只在```while```语句中声明后的位置可见。

当不确定需要迭代多少次时适合使用```while```循环。

```c++
vector<int> v;
int i;
while (cin >> i) {
    v.push_back(i);
}
auto beg = v.begin();
while (beg != v.end() && *beg >= 0) {
    ++beg;
}
if (beg == v.end()) {
    // 处理结果。
}
```

### 传统```for```语句

**```for```语句（```for``` statement）**的形式为：

```c++
for (init-statement; condition; expression)
    statement
```

其中```init-statement```与```statement```为语句，```init-statement```只能为一条声明语句、表达式语句或空语句（分号已提供），```condition```表示可转换为```bool```类型的表达式或初始化的变量声明，```expression```为表达式。关键字```for```及括号里的内容被称为```for```语句头（```for``` header）。程序首先执行```init-statement```语句，然后不断迭代：判断```condition```是否为```true```，如果为```true```，则执行```statement```，然后执行```expression```；否则跳过```statement```执行下一语句。定义在```for```语句头中的变量在```for```语句结束后销毁，```statement```中的变量每次迭代都经历从创建到销毁的过程。这两种变量只在```for```语句中声明后的位置可见。

```c++
for (decltype(s.size()) index = 0; index != s.size() && !isspace(s[index]); ++index) {  // s为string。
    s[index] = toupper(s[index]);
}
```

```c++
for (decltype(v.size()) i = 0, sz = v.size(); i != sz; ++i) {  // v为vector<int>；init-statement部分只能有一条语句。
    v.push_back(v[i]);
}
```

```init-statement```、```condition```与```expression```都可以省略（但是```for```语句头中的两个分号不可以省略）；省略```condition```相当于条件永远为```true```。

```c++
auto beg = v.begin();  // v为vector。
for (/* init-statement为空 */; beg != v.end() && *beg >= 0; ++beg);
```

```c++
for (int i = 0; /* condition为空 */; ++i) {
	// 循环体负责终止迭代过程。
}
```

```c++
vector<int> v;
for (int i; cin >>i; /* expression */) {
     v.push_back(i);
}
```

### 范围```for```语句

**范围```for```语句（range ```for``` statement）**的形式为：

```c++
for (declaration : expression)
	statement
```

其中```declaration```定义一个变量，```expression```为表达式，且必须表示序列对象（定义了```begin```与```end```成员），```statement```为语句。程序不断迭代：依次用```expression```中的元素初始化```declaration```，然后执行```statement```。

```c++
vector<int> v = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
for (auto &r : v) {
    r *= 2;  // 将v中的每个元素的值加倍，注意r必须是引用。
}

// 以上for循环等价于：
for (auto beg = v.begin(), end = v.end(); beg != end; ++beg) {
    auto &r = *beg;
    r *= 2;
}
```

范围```for```语句中不能增加或修改```expression```中的元素，否则```expression```的尾后迭代器可能失效。

### ```do while```语句

**```do while```语句（```do while``` statement）**的形式为：

```c++
do
	statement
while (condition);
```

其中```statement```为语句，```condition```为可转换为```bool```类型的表达式。程序首先执行```statement```，然后不断迭代：判断```condition```是否为```true```，如果为```true```，执行```statement```；否则迭代结束，执行下一语句。```condition```中使用的变量必须定义在循环体外。

```c++
string rsp;
do {
	cout << "please enter two values: ";
    int val1 = 0, val2 = 0;
    cin >> val1 >> val2;
    cout << "The sum of " << val1 << " and " << val2 << " = " << val1 + val2 << "\n\n" << "More? Enter yes or no: ";
    cin >> rsp;
} while (!rsp.empty() && rsp[0] != 'n');
```

## 跳转语句

### ```break```语句

**```break```语句（```break``` statement）**用于迭代语句循环体或```switch```语句中，用于跳出离其最近的迭代语句或```switch```语句，并从这些语句之后的第一条语句继续执行（如果有的话）。

```c++
string buf;
while (cin >> buf && !buf.empty()) {
    switch (buf[0]) {
        case '-':
            for (auto it = buf.begin() + 1; it != buf.end(); ++it) {
                if (*it == ' ')
                    break;  // 只终止for循环，不会终止case分支，更不会终止while循环
                // ...
            }
            break;  // 终止switch语句
        case '+':
            // ...
    }
}
```

### ```continue```语句

**```continue```语句（```continue``` statement）**用于迭代语句中，用于终止最近的迭代语句的当前迭代并立即开始下一次迭代。

```c++
string buf;
while (cin >> buf && !buf.empty()) {
    if (buf[0] != '_')
        continue;
    // ...
}
```

### ```goto```语句

**```goto```语句（```goto``` statement）**作用是从```goto```无条件跳转到同一函数内的另一条语句，其形式为：```goto label;```。其中```label```为标识语句的标识符。执行该语句导致程序跳转到**带标签语句（labeled statement）**：```label: statement```。其中```statement```为语句，```label```可以是任意合法的标识符（即使与其他实体重名），只要同一函数内各个标签互不相同。这两条语句必须位于同一个函数内。不允许定义了```goto```语句但没有定义对应的带标签语句，但是可以定义带标签语句而不定义对应的```goto```语句。

如果```goto```语句导致程序可能不执行某个变量的初始化语句，但是可能在其他地方用到该变量，则报错（即不允许控制流将初始化的变量从作用域之外转移到作用域之内）。如果允许这种情况发生，那么可能使用了期望被初始化但未被初始化的变量。

> As with a switch statement, a goto cannot transfer control from a point where an initialized variable is out of scope to a point where that variable is in scope:

goto语句容易导致程序难以理解与修改，因此应尽量避免使用。

# 函数

函数（function）是一个命名的代码块，通过调用函数执行其中代码。

函数的定义如下：

```c++
return_type name(parameter_list) {
    body
} 
```

其中，```return_type```为类型，称为返回类型（return type）；```name```为函数名；```parameter_list```为形参（parameter）列表，每个形参定义了（且只定义）一个变量，以逗号分隔；```body```为函数体（function body），包含若干语句（可以为空）。形参列表中定义的变量在函数体中可见，定义在函数中的变量仅在该函数中可见，一旦函数结束这些变量都被销毁。如果某个形参在函数体中没有被使用，则形参的变量名可以省略。

相应的，函数的声明如下：```return_type name(parameter_list);```。函数的声明会忽略所有形参中的变量名，变量名可以省略，如果两个函数声明只有形参变量名有区别，则这两个函数声明实际上是相同的声明。函数定义也是函数声明。函数声明也被称为**函数原型（function prototype）**。

与变量类似，建议在头文件中声明函数，在源文件中定义函数，以便确保同一函数的声明一致，并且当函数接口改变时只需要改变一条声明。定义函数的源文件应该包含（```#include```）包含该函数声明的头文件，以便让编译器验证函数的定义与声明是否匹配。

函数通过**调用运算符（call operator）**（```()```）被执行，函数的调用方式为：```name(argument_list)```。其中，```argument_list```为实参（argument）列表，每个实参为一个变量，以逗号分隔。当一个主调函数（calling function）调用被调函数时（called function），主调函数用实参初始化对应的形参，然后将控制权移交到被调函数，执行被调函数，最后返回类型为```return_type```的值（如果```return_type```不为```void```），接着执行主调函数。

## 实参传递

实参为形参的初始值，每个实参初始化对应位置的形参，初始化规则等同于变量的初始化规则。该过程中，实参的求值顺序不定。由于实参与形参存在对应关系，实参与形参的数量必须相同，即使某个形参没有被使用或变量名被省略，所有形参都必须被初始化。

形参列表可以为空，此时```parameter_list```可以为空，也可以显式表示为```void```。

```c++
void f1() { /* ... */ }  // 隐式定义空形参列表。
void f2(void) { /* ... */ }  // 显式定义空形参列表。
```

如果形参为引用，则我们称对应的实参“被引用传递”（passed by reference）或“传引用调用（called by reference）”；如果实参的值被拷贝给形参，则我们称对应的实参“被值传递（passed by value）”或“传值调用（called by value）”。

### 传值实参

当使用实参初始化非引用类型的形参时，初始值被拷贝给变量。此时，对形参的改变不会影响实参。如果想要改变实参，可以传递指向实参的指针，此时指针值被拷贝。

```c++
void reset(int *ip) {
    *ip = 0;  // 改变了ip所指对象的值，即实参所指对象的值。
    ip = 0;  // 改变形参ip的值，但是实参ip没有被影响。
}
```

### 传引用实参

通过将参数定义为引用，可以修改实参的值。

```c++
void reset(int &i) {
    i = 0;
}
```

将实参定义为引用类型可以避免对实参的拷贝，如果实参为类类型对象或容器对象，这将有助于提高程序效率，另外某些类类型不支持拷贝。

```c++
bool isShorter(const string &s1, const string &s2) {
    return s1.size() < s2.size();
}
```

引用形参可以返回额外信息。

```C++
string::size_type find_char(const string &s, char c, string::size_type &occurs) {
	auto ret = s.size();
	occurs = 0;
	for (decltype(ret) i = 0; i != s.size(); ++i) {
        if (s[i] == c) {
            if (ret == s.size())
            ret = i;
            ++occurs;
		}
	}
	return ret;
}

auto index = find_char(s, 'o', ctr);  // s为string，ctr为string::size_type且初始设置为0。
```

如果某个形参不会被修改，则尽量将其定义为常量引用，否则会限制函数所能接受的实参类型，同时也会误导函数调用者，让其以为函数可以修改实参的值。如果一个函数正确将其形参定义为常量引用，而其调用一个函数没有正确将其形参定义为常量引用的函数，则调用可能发生错误。

```c++
string::size_type find_char(string &s, char c, string::size_type &occurs);

string::size_type ctr = 0;
find_char("Hello World", 'o', ctr);  // 错误，无法将const char*绑定到string&。
```

```c++
bool is_sentence(const string &s) {
    string::size_type ctr = 0;
    return find_char(s, '.', ctr) == s.size() - 1 && ctr == 1;  // 错误。
}
```

### 数组形参

由于无法拷贝数组以及大多数情况下使用数组时会将其转换为指针，因此无法定义数组形参。将形参写成数组形式实际上定义了指针形参，此时可以省略数组维度。如果传递给形参一个数组实参，则该实参也转换为指针。

```c++
// 3个等价的函数声明。
void print(const int*);
void print(const int[]);
void print(const int[10]);

int i = 0, j[2] = {0, 1};
print(&i);  // 正确。
print(j);  // 正确。
```

有三种常见的管理指针形参的方法（主要用于确定数组的尺寸）。

- 如果数组本身包含一个结束标记，则可使用该标记指定数组长度。

```c++
void print(const char *cp) {
    if (cp)
        while (*cp)
            cout << *cp++;
}
```

- 传递指向数组首元素与尾后元素的指针，该方法收到标准库（容器）技术的启发。

```c++
void print(const int *beg, const int *end) {
    while (beg != end)
        cout << *beg++ << endl;
}

int j[2] = {0, 1};
print(begin(j), end(j));
```

- 显式传递一个表示数组大小的形参。

```c++
void print(const int ia[], size_t size) {
    for (size_t i = 0; i != size; ++i) {
        cout << ia[i] << endl;
    }
}

int j[] = {0, 1};
print(j, end(j) - begin(j));
```

如果数组形参中的元素不会被修改，则尽量将其定义为指向常量的指针。

可以将形参定义为数组的引用，显然此时不能省略数组维度。

```c++
void print(int (&arr)[10]) {  // 只能传递维度为10的数组。
    for (auto elem: arr) {
        cout << elem << endl;
    }
}
```

如果要传递给函数多维数组，则从数组第二维开始的大小都是数组类型的一部分，不能省略。

```c++
void print(int (*matrix)[10], int rowSize) { /* ... */ }
void print(int matrix[][10], int rowSize) { /* ... */ }  // 等价定义。
```

### 省略符形参

省略符形参（ellipsis parameter）是为了便于C++程序访问C代码而设置的，这些代码使用了名为```varargs```的C标准库功能。

> Ellipsis parameters are in C++ to allow programs to interface to C code that uses a C library facility named ```varargs```.

省略符形参应该仅仅用于C与C++通用的类型。特别地，大多数类类型对象在传递给省略符形参时无法得到正确拷贝。

> Ellipsis parameters should be used only for types that are common to both C and C++. In particular, objects of most class types are not copied properly when passed to an ellipsis parameter.

省略符形参只能作为形参列表的最后一个元素，有两种形式：```void foo(parm_list, ...)```（指定了```foo```部分形参的类型，对应的实参会执行正常的类型检查，形参声明后的```,```是可选的）；```void foo(...)```。省略符形参对应的实参不会执行类型检查。

### 默认实参

可以为函数形参赋初始值，调用函数时可以省略对应的实参，此时该实参默认取得该初始值，该值被称为**默认实参（default argument）**。

```c++
typedef string::size_type sz;
string screen(sz ht=24, sz wid=80, char backgrnd=' ');

string window;
window = screen();  // 等价于screen(24, 80, ' ')。
window = screen(66);  // 等价于screen(66, 80, ' ')。
window = screen(66, 256);  // 等价于(66, 256, ' ')。
window = screen(66, 256, '#');
```

默认实参只能位于形参列表的尾部，函数调用时实参按照位置解析，默认实参负责填补函数调用缺少的尾部实参。

```c++
window = screen(, , '?');  // 错误。
window = screen('?');  // 等价于screen('?', 80, ' ')，语法正确但是不符合预期。
```

在给定作用域中，一个形参只能被赋予一次默认实参，即函数的后续声明只能为之前那些没有默认值的形参添加默认实参。

```c++
string screen(sz, sz, char=' ');
string screen(sz, sz, char='*');  // 错误。
```

局部变量不能作为默认实参，除此以外，只要表达式的类型能够转换为形参所需类型，该表达式就能作为默认实参。用作默认实参的名字在函数声明所在的作用域内解析，而名字的求值过程发生在函数调用时。

```c++
sz wd = 80;
char def = ' ';
sz ht();
string screen(sz=ht(), sz=wd, char=def);  // 调用screen(ht(), 80, ' ');

void f2() {
    def = '*';
    sz wd = 100;
    window = screen();  // 调用screen(ht(), 80, '*');
}
```

通常应该在定义在头文件中的函数声明中指定默认实参。

## 返回类型

大多数类型都能作为函数的返回类型。一种特殊的返回类型为```void```，表示函数不返回任何值。函数的返回类型不能为数组类型或函数类型。

函数通过```return```语句终止当前正在执行的函数并将控制权返回到调用该函数的地方。```return```语句有两种形式：```return;```与```return expression;```。第一种形式表示终止当前函数并且不返回任何值，第二种形式表示当前函数终止并返回值```expression```。

如果函数的返回类型为```void```，则其必须使用```return```语句的第一种形式，此时```return```语句可以省略，因为这类函数的最后语句后会隐式地执行```return```。

如果函数的返回类型不是```void```，则在每个可能终止的地方，必须使用```return```语句的第二种形式返回值，且函数的返回值的类型必须与返回类型一致，或者可以转换为返回类型。函数的返回值用于初始化调用点的一个临时量，该临时量就是函数调用结果。

> Values are returned in exactly the same way as variables and parameters are initialized: The return value is used to initialize a temporary at the call site, and that temporary is the result of the function call.

```c++
void swap(int &v1, int &v2) {
    if (v1 == v2)
        return;  // 显式返回。
    int tmp = v2;
    v2 = v2;
    v1 = tmp;
    // 此处隐式返回。
}
```

```c++
bool str_subrange(const string &str1, const string &str2) {
    if (str1.size() == str2.size())
        return str1 == str2;
    auto size = (str1.size() < str2.size()) ? str1.size() : str2.size();
    for (decltype(size) i = 0; i != size; ++i) {
        if (str1[i] != str2[i])
            return;  // 错误，没有返回值。
    }
    // 错误，没有返回语句。编译器可能检查不出该错误。
}
```

如果函数返回引用类型，则返回值被绑定到调用点的临时量；否则，返回值被拷贝到该调用点的临时量。

```c++
const string &shorterString(const string &s1, const string &s2) {
    return s1.size() <= s2.size() ? s1: s2;
}

auto sz = shorterString(s1, s2).size();  // s1与s2为string。由于点运算符与调用运算符优先级相同并且两者都左结合。因此可以使用函数调用结果来访问结果对象的成员。
```

如果函数返回引用类型，则调用该函数得到左值，否则得到右值。

```c++
char &get_val(string &str, string::size_type ix) {
    return str[ix];
}

int main() {
    string s("a value");
    cout << s << endl;
    get_val(s, 0) = 'A';  // get_val返回s[0]的引用，该语句将s[0]的值改为A
    cout << s << endl;
    return 0;
}
```

可以列表初始化返回值。如果列表为空，则调用点的临时量被值初始化，否则被返回值取决于函数的返回类型。如果返回类型为内置类型，则列表最多包含一个值，且不允许窄化转换；如果返回类型为类类型，则由类本身定义如何使用初始值。

```c++
vector<string> process() {
    // ...
    // expected与actual为string
    if (expected.empty())
        return {};
    else if (expected == actual)
        return {"functionX", 'okay'};
    else
        return {"functionX", expected, actual};
}
```

不要返回函数内局部对象的引用或指针。因为函数被返回时，局部对象已经被销毁了。

```c++
const string &manip() {
    string ret;
    if (!ret.empty())
        return ret;  // 错误，返回局部变量ret。
    else:
    	return "Empty";  // 错误，"Empty"也是局部变量。
}
```

函数可以返回指向数组的指针或数组引用，形式为：

```c++
type (*function(parameter_list))[dimension]  // 函数function的参数列表为parameter_list，返回指向类型为type[dimension]的数组的指针。
type (&function(parameter_list))[dimension]  // 函数function的参数列表为parameter_list，返回类型为type[dimension]的数组的引用。
```

```c++
// 使用类型别名简化表达。
using arrT = int[10];
arrT *func(int i);
```

```c++
// 使用类型推导。
int odd[] = {1, 3, 5, 7, 9};
int even[] = {0, 2, 4, 6, 8};
decltype(odd) *arrPtr(int i) {
    return (i % 2) ? &odd : &even; 
}
```

### 尾置返回类型

任何函数都可以使用**尾置返回类型（trailing return type）**取代常规的返回类型表示形式，使用尾置返回类型定义或声明函数的方法如下：

```c++
auto name(parameter_list) -> return_type {
    body
}
```

```c++
auto name(parameter_list) -> return_type;
```

尾置返回类型有利于简化某些复杂返回类型的表达。

```c++
auto func(int i) -> int(*)[10] {
    // ...
}
```

## 函数重载

出现在同一作用域内的名字相同的不同函数被称为**重载（overloaded）**函数。两个重载函数的形参列表必须不同，即形参数量不同或某个对应位置上的形参类型不同。在考虑形参类型时忽略形参的顶层```const```。不能定义名字相同且仅返回类型不同、仅形参名字不同或仅形参顶层```const```不同的重载函数。

尽量避免使用重载函数去实现不相似的操作。

```c++
// 一组负责移动屏幕光标的函数
Screen &moveHome();
Screen &moveAbs(int, int);
Screen &moveRel(int, int, string direction);

// 避免使用如下形式，因为这些函数移动光标的方式不同，且丢失了名字中本来拥有的信息。
Screen &move();
Screen &move(int, int);
Screen &move(int, int, string direction);
```

由于C++中名字查找发生在类型检查前，不同作用域内的函数不构成重载关系，此时内层作用域中的名字会隐藏外层作用域中的名字。

```c++
string read();
void print(const string&);
void print(double);
void fooBar(int ival) {
    bool read = false;
    string s = read();  // 错误，read为布尔类型变量。
    void print(int);
    print("Value: ");  // 错误，print(const string&)被隐藏了。
    print(ival);
    print(3.14);  // 调用print(int)而不是print(double)。
}
```

### 重载匹配

#### 匹配步骤

函数匹配首先选定本次调用对应的重载函数集，集合中的函数被称为**候选函数（candidate function）**。候选函数有两个特征：与被调用的函数同名；声明在函数调用点可见。

然后通过考察调用提供的实参从候选函数中选出能匹配这组实参调用的函数，这些函数被称为**可行函数（viable function）**。可行函数有两个特征：形参数量于本次调用提供的实参数量相同；实参类型与对应的形参类型可匹配。

最后寻找最佳匹配。最佳匹配有两个特征：函数的每个实参的匹配都不劣于其他可行函数需要的匹配；至少有一个实参的匹配优于其他可行函数提供的匹配。实参到形参的类型转换优先级越高，匹配越优。

如果以上任意过程失败，则匹配失败，程序报错。

#### 实参到形参类型转换优先级

1. 精确匹配，包括：
   - 实参与形参类型相同。
   - 实参从数组或函数类型转换为对应的指针类型。
   - 通过顶层```const```转换实现的匹配。
2. 通过底层```const```转换实现的匹配。
3. 通过（整型）提升实现的匹配。
4. 通过算术类型转换或指针转换实现的匹配。
5. 通过类类型转换实现的匹配。

如果实参使用了默认实参，则其一定为精确匹配。

```c++
void f();
void f(int);
void f(int, int);
void f(double, double=3.14);

// 候选函数：4个名为f的函数。
// 可行函数：void f(int)与void f(double, double=3.14)。
// 最佳匹配：void f(double, double=3.14);
f(5.6);

f(42. 2.56);  // 二义性调用。
```

```C++
void ff(int);
void ff(short);

ff('a');  // 调用f(int)，需要整型提升。
```

```c++
void manip(long);
void manip(float);

manip(3.14);  // 二义性调用：从double转换为long与float的优先级一样，都是第4级。
```

```c++
Record lookup(Account&);
Record lookup(const Account&);

const Account a;
Account b;
lookup(a);  // 调用Record lookup(const Account&)。
lookup(b);  // Record lookup(Account&)。
```

## 特殊函数

### 内联函数

通过对函数的返回类型前置```inline```将其声明为**内联（inline）**函数，此时该函数会在其每个调用点内联地展开，以避免函数调用的开销。

内联说明只是向编译器发出的一个请求，编译器可以选择忽略这个请求。

```c++
cout << shorterString(s1, s2) << endl;

// 假设shorterString被定义为内联函数，则其在编译过程中被展开为类似于下面的形式：
cout << (s1.size() < s2.size() ? s1 : s2) << endl;
```

内联函数一般规模较小、流程直接、频繁调用。很多编译器不支持内联递归函数。

与其他函数不同，内联函数可以在程序中多次多次定义。因为编译器要想展开函数（expand the code）需要函数的定义，而不能仅仅是声明。但是给定的```inline```函数的多个定义必须完全一致。因此，```inline```函数通常定义在头文件中。

### ```constexpr```函数

通过在函数的返回类型前加上关键字```constexpr```将其声明为**```constexpr```函数**，```constexpr```函数是能用于常量表达式的函数。

```constexpr```函数的返回类型及所有形参必须是字面值类型，函数体内除了仅有的一条```return```语句外，其他语句在运行时不能执行任何操作，例如空语句、类型别名。

当调用```constexpr```函数时，编译器把对```constexpr```函数的调用替换成其结果值。为了能在编译过程中随时展开，```constexpr```函数被隐式指定为内联函数。

```constexpr```函数的返回值可以为非常量；当```constexpr```函数的实参全部为常量表达式，则其返回值为常量表达式，否则不是。

```c++
// 当且仅当arg为常量表达式，scale(arg)为常量表达式。
constexpr size_t scale(size_t cnt) {
    return new_sz() * cnt;
}

int arr[scale(2)];  // 正确，scale(2)为常量表达式。
int i = 2;
int a2[scale(i)];  // 错误，scale(i)不是常量表达式。
```

与其他函数不同，```constexpr```函数可以在程序中多次多次定义。因为编译器要想展开函数（expand the code）需要函数的定义，而不能仅仅是声明。但是给定的```constexpr```的多个定义必须完全一致。因此，```constexpr```通常定义在头文件中。

### ```main```函数

```main```函数为程序执行的入口，操作系统通过调用```main```函数来运行C++程序。

```main```函数的返回类型为```int```。```main```函数可以没有相应的```return```语句直接结束，此时编译器将隐式在程序结尾处插入一条返回```0```的```return```语句。

```main```函数的返回值可以看作是状态指示器，返回```0```表示执行成功，返回其他大多数值表示执行失败。非0值的含义依机器而定，为了使返回值与机器无关，```cstdlib```头文件定义了两个预处理变量```EXIT_FAILURE```与```EXIT_SUCCESS```指示执行成功与失败。

```c++
int main()　{
    if (some_failure) {
        return EXIT_FAILURE;  // 该预处理变量指示执行失败。
    }   
    else {
        return EXIT_SUCCESS;  // 该预处理变量指示执行成功。
    }
}
```

```main```函数的形参列表有两种：一种为空；另一种接受两个参数，其类型分别为```int```与```char **```（或写成```char**```）。这两个参数用于处理命令行选项。其中，第二个形参为一个指向C风格字符串的数组，其第一个元素指向程序名或空字符串，接下来的元素依次传递命令行提供的实参，最后一个指针之后的元素保证为0；第一个形参表示数组中字符串的数量（比数组维度小1）。

带有两个形参的```main```函数一般写作：

```c++
int main(int argc, char *argv[]) { /* ... */ }
```

或等价的：

```c++
int main(int argc, char **argv) { /* ... */ }
```

假设命令行选项为：```prog -d -o ofile data0```，则：

```c++
argv[0] = "prog";  // argv[0]也可能指向一个空字符串。
argv[1] = "-d";  // 可选的实参从argv[1]开始。
argv[2] = "-o";
argv[3] = "ofile";
argv[4] = "data0";
argv[5] = 0;

argc = 5;
```

## 函数指针

函数指针指向某种特定类型的函数，为特殊的指针。函数的类型由其返回类型与形参类型决定。

```c++
bool (*pf)(const string&, const string&);  // pf可指向参数类型为两个const&、返回类型为boo类型的函数。
```

当将函数名作为一个值使用时，其自动转换为指针。

```c++
bool lengthCompare(const string&, const string&);

// 两种等价的写法。
pf = lengthCompare;
pf = &lengthCompare;
```

可以直接使用指向函数的指针调用该函数，无需提前解引用指针。

```c++
// 三种等价的调用。
bool b1 = pf("hello", "goodbye");
bool b2 = (*pf)("hello", "goodbye");
bool b3 = lengthCompare("hello", "goodbye");
```

指向不同的函数的指针间不存在转换规则，但是可以令指向函数的指针为空指针。

如果定义了指向重载函数的指针，则编译器通过指针类型决定选用哪个函数，指针类型必须与重载函数中的某个精确匹配。

```c++
void ff(int*);
void ff(unsigned int);

void (*pf1)(unsigned int) = ff;  // pf1指向void ff(unsigned int)。
void (*pf2)(int) = ff;  // 错误，参数类型不匹配。
double (*pf3)(int*) = ff;  // 错误，返回类型不匹配。
```

### 函数指针形参

函数的形参不能为函数类型，但是可以是指向函数的指针。形参可以写成函数类型形式，此时其表示函数指针；可以将函数名作为实参，此时其自动转换为函数指针。

```c++
void useBigger(const string &s1, const string &s2, bool pf(const string&, const string&));

// 等价的写法
void useBigger(const string &s1, const string &s2, bool (*pf)(const string&, const string&));

useBigger(s1, s2, lengthCompare);
```

可以定义类型别名以简化书写形式：

```c++
typedef bool Func(const string&, const string&);  // Func为函数类型
typedef decltype(lengthCompare) Func2;  // 等价的类型。

typedef bool (*FuncP)(const string&, const string&);  // FuncP为函数指针类型。
typedef decltype(lengthCompare) *FuncP2;  // 等价的类型。

void useBigger(const string&, const string&, Func);
void useBigger(const string&, const string&, FuncP2);  // 等价的声明。
```

### 返回指向函数的指针

函数的返回类型不能是函数，但可以是指向函数的指针，此时编译器不会自动将函数类型当成对应的指针类型。

```c++
int (*f1(int))(int*, int);  // f1返回int(*)(int*, int)。如果f1两端的括号省略，则表示其返回函数，错误。

// 使用类型别名简化定义。
using F = int(int*, int);
using PF = int(*)(int*, int);

FF f1(int);
F f1(int);  // 错误，f1为函数类型而非函数指针类型。
F *f1(int);

// 使用尾置返回类型。
auto f1(int) -> int(*)(int*, int);

// 使用auto或decltype。
string::size_type sumLength(const string&, const string&);
string::size_type largerLength(const string&, const string&);
decltype(sumLength) *genFcn(const string&);  // 注意需要显式表明返回函数指针。
```

## 可调用对象

函数与函数指针都是**可调用对象（callable object）**。对于一个对象或表达式，如果可以对其应用调用运算符（```()```），则它为可调用的。

> An object or expression is callable if we can apply the call operator (§ 1.5.2, p. 23) to it.

### 谓词

**谓词（predicate）**是可调用的表达式，它的返回结果是可用作条件的值。

标准库算法使用的谓词分为两类：**一元谓词（unary predicate）**与**二元谓词（binary predicate）**。所谓元，就是谓词的形参数量。

```c++
// 根据单词长度排序单词。
// sort接受一个二元谓词。
bool isShorter(const string &s1, const string &s2) {
    return s1.size() < s2.size();
}
sort(words.begin(), words.end(), isShorter);  // words为vector<string>。
stable_sort(words.begin(), words.end(), isShorter);  // 另一个版本：保持等长元素相对顺序。
```

### lambda表达式

**lambda表达式（lambda expression）**可以被看作匿名的内联函数。一个lambda表达式具有形式：```[capture list] (parameter list) -> return type { function body }```。其中```capture list```（捕获列表）为lambda所在函数中定义的局部变量的列表（通常为空），```parameter list```（形参列表）、```return type```（返回类型）与```function body```（函数体）的含义同普通函数。lambda必须使用尾置返回类型。

lambda表达式的形参列表（如果为空的话）与返回类型可以省略，但是捕获列表与函数体不可省略。

```c++
auto f = [] { return 42; }
```

lambda表达式的调用方式同普通函数。

```c++
cout << f() << endl;
```

#### 实参传递

lambda的实参传递方式同普通函数。

与普通函数不同的是，lambda不能有默认实参，因此一个lambda调用的实参数量永远与形参数量相同。

```c++
stable_sort(words.begin(), words.end(), [] (const string &a, const string &b) { return a.size() < b.size(); });  // 根据单词长度排序单词，将lambda作为二元谓词。
```

#### 捕获列表

lambda可以出现在函数体内，如果lambda想要使用局部变量，则必须在捕获列表中包含这些变量。捕获列表指引lambda在其内部包含访问局部变量所需的信息。

> The capture list directs the lambda to include information needed to access those variables within the lambda itself.

```c++
// 统计大于等于给定长度的单词数量。
void biggies(vector<string> &words, vector<string>::size_type sz) {
    elimDups(words);
    stable_sort(words.begin(), words.end(), [] (const string &a, const string &b) { return a.size() < b.size(); });
    auto wc = find_if(words.begin(), words.end(), [sz] (const string &a) { return a.size() >= sz; });  // lambda表达式必须在捕获列表中包含要使用的局部变量。
    auto count = words.end() - wc;
    cout << count << " " << make_plural(count, "word", "s") << " of length " << sz << " or longer" << endl;
    for_each(wc, words.end(), [] (const string &s) { cout << s << " "; });
    cout << endl;
}

// 单词序列（字典序）排序并去重。
void elimDups(vector<string> &words) {
    sort(words.begin(), words.end());
    auto end_unique = unique(words.begin(), words.end());
    words.erase(end_unique, words.end());
}

// 输出单词正确的单复数形式。
string make_plural(size_t ctr, const string &word, const string &ending) {
	return (ctr > 1) ? word + ending : word;
}
```

lambda捕获列表可具有如下形式：

<table>
    <tr>
        <th>形式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>[]</td>
        <td>空捕获列表。lambda不能使用所在函数中的变量。</td>
    </tr>
    <tr>
        <td>[names]</td>
        <td>names为逗号分隔的、所在函数内的名字列表。默认情况下，捕获列表中的变量被拷贝（值捕获方式），如果名字前置“&amp;”，则采用引用捕获方式。</td>
    </tr>
    <tr>
        <td>[&amp;]</td>
        <td>隐式捕获列表，采用引用捕获方式。lambda体内使用的来自所在函数的实体采用引用方式使用。</td>
    </tr>
    <tr>
        <td>[=]</td>
        <td>隐式捕获列表，采用值捕获方式。lambda体内使用的来自所在函数的实体被拷贝进lambda体。</td>
    </tr>
    <tr>
        <td>[&amp;, identifier_list]</td>
        <td>identifier_list为逗号分隔的、所在函数内的名字列表，名字不允许前置&amp;，这些变量被值捕获，任何其他隐式捕获的变量被引用捕获。</td>
    </tr>
    <tr>
        <td>[=, identifier_list]</td>
        <td>identifier_list为逗号分隔的、所在函数内的名字列表，名字必须前置&amp;，且不能包含名字“this”，这些变量被引用捕获，任何其他隐式捕获的变量被值捕获。</td>
    </tr>
</table>

```c++
void fcn1() {
    size_t v1 = 42;
    auto f = [v1] { return v1; };
    v1 = 0;
    auto j = f();  // j为42。
}

void fcn2() {
    size_t v1 = 42;
    auto f2 = [&v1] { return v1; };
    v1 = 0;
    auto j = 42();  // j为0。
}
```

```c++
void biggies(vector<string> &words, vector<string>::size_type sz, ostream &os=cout, char c=' ') {
    // ...
    for_each(words.begin(), words.end(), [&os, c] (const string &s) { os << s << c; });  // 重写for_each，此时必须使用引用捕获。
}
```

```c++
wc = find_if(words.begin(), words.end(), [=](const string &s) { return s.size() >= sz; });  // 隐式捕获列表。
```

```c++
void biggies(vector<string> &words, vector<string>::size_type sz, ostream &os = cout, char c = ' ') {
 // ...
    for_each(words.begin(), words.end(), [&, c](const string &s) { os << s << c; });  // 重写for_each。
    for_each(words.begin(), words.end(), [=, &os](const string &s) { os << s << c; });  // 另一种重写for_each的方式。
}
```

采用引用捕获时，要保证在lambda执行时该变量是存在的。类似于函数不能返回局部变量的引用，如果函数返回lambda，则此lambda不能包含引用捕获。

尽量减少lambda捕获的数据量，且尽可能避免捕获指针、引用或采用引用捕获方式。

默认情况下，被值捕获的变量，lambda不能改变它的值。如果要改变它的值，则必须在形参列表后加上```mutable```。

```c++
void fcn3() {
    size_t v1 = 42;
    auto f = [v1] () mutable { return ++v1; };
    v1 = 0;
    auto j = f();  // j为43
}
```

一个引用捕获的变量能否被修改取决于其绑定的对象是不是```const```的。

```c++
void fcn4() {
    size_t v1 = 42;
    auto f2 = [&v1] { return ++v1; };
    v1 = 0;
    auto j = f2();  // j为1。
}
```

#### 返回类型

如果lambda省略了返回类型，且lambda体只包含一条```return```语句，lambda根据函数体内的代码推断出返回类型；如果lambda体内还包含其他语句，则该lambda被假定返回void，因此这样的lambda不能返回值。如果要让这样的lambda返回值，则必须为其显式提供返回类型。

```c++
// vi为vector<int>。

transform(vi.begin(), vi.end(), vi.begin(), [](int i) { return i < 0 ? -i : i; });  // 正确。
transform(vi.begin(), vi.end(), vi.begin(), [] (int i) { if (i < 0) return -i; else return i; });  // 错误，不能推断lambda的返回类型。
```

```c++
transform(vi.begin(), vi.end(), vi.begin(), [] (int i) -> int { if (i < 0) return -i; else return i; });
```

#### 类型

lambda表达式是函数对象。当编写一个lambda时，编译器将该表达式翻译成一个未命名类的未命名对象。其中，lambda表达式的形参对应未命名对象的调用运算符的形参。默认情况下，lambda表达式不能改变它捕获的变量，因此默认情况下，由lambda生成的类中的调用运算符是```const```成员函数。

```c++
// 一个lambda表达式。
[] (const string &a, const string &b) { return a.size() < b.size(); };

// 其行为类似以下类的未命名的对象：
class ShorterString {
public:
    bool operator()(const string &s1, const string &s2) const {
        return s1.size() < s2.size();
    }
};

stable_sort(words.begin(), words.end(), [] (const string &a, const string &b) { return a.size() < b.size(); });
stable_sort(words.begin(), words.end(), ShorterString());  // 使用函数对象重写。
```

如果lambda表达式通过引用捕获变量，则由程序负责确保lambda执行时引用所绑定的变量存在。因此编译器允许直接使用该引用而不用在生成的类中将其存储为数据成员。

反之，通过值捕获的变量被拷贝到lambda中，因此lambda产生的类必须为每个值捕获的变量建立对应的数据成员，这些类同时有一个构造函数并使用捕获的变量的值来初始化这些数据成员。

```c++
// 一个lambda表达式。
[sz] (const string &a) { return a.size() >= sz; };  // sz为size_t。

// 生成一个类类似于：
class SizeComp {
    SizeComp(size_t n): sz(n) {}
    bool operator()(const string &s) const {
        return s.size() >= sz;
    }
private:
    size_t sz;
};

auto wc = find_if(words.begin(), words.end(), [sz] (const string &a) { return a.size() >= sz; });
auto wc = find_if(words.begin(), words.end(), SizeComp());  // 使用函数对象重写。
```

### ```bind```

```bind```函数定义在头文件```functional```中，可以被看做一个通用的函数适配器。其接受一个可调用对象，生成一个新的可调用对象来“适应”原对象的形参列表。

调用```bind```的一般形式为：```bind(callable, arg_list)```，其中```callable```为一个可调用对象，```arg_list```为逗号分隔的实参列表，对应```callable```中的形参。调用该```bind```返回一个新的可调用对象```newCallable```。当调用```newCallable```时，```newCallable```会调用```callable```，并传递给它```arg_list```中的实参。

```arg_list```中的实参可以包含形如```_n```的名字，其中```_n```为一个整数，定义在```std::placeholders```的命名空间中。这些实参为“占位符”，表示```newCallable```中的形参，它们占据了将要传递给```newCallable```的实参的位置。数值```_n```表示生成的可调用对象中的形参的位置。

```c++
auto g = bind(f, a, b, _2, c, _1);
g(X, Y);  // 将g(_1, _2)映射为：f(a, b, _2, c, _1)，因此该表达式调用：f(a, b, _Y, c, X)。
```

```c++
bool check_size(const string &s, string::size_type sz) {
    return s.size() >= sz;
}

auto check6 = bind(check_size, _1, 6);

string s = "hello";
bool b1 = check6(s);  // 调用check_size(s, 6)。

auto wc = find_if(words.begin(), words.end(), bind(check_size, _1, sz));  // sz为string::size_type。通过bind传递一元谓词，代替对应的lambda。
```

```c++
sort(words.begin(), words.end(), isShorter);  // 单词长度顺序排序。
sort(words.begin(), words.end(), bind(isShorter(_2, _1)));  // 使用bind实现单词长度逆序排序。
```

默认情况下，```bind```将非占位符实参拷贝到```bind```返回的可调用对象中。如果要避免拷贝，可以使用```ref```函数，它返回一个对象，包含给定的、可拷贝的引用。```cref```函数类似，但是生成一个保存常量引用的类。这些函数也位于```functional```头文件中。

> The ```ref``` function returns an object that contains the given reference and that is itself copyable. There is also a ```cref``` function that generates a class that holds a reference to ```const```. Like ```bind```, the ```ref``` and ```cref``` functions are defined in the ```functional``` header.

```c++
ostream &print(ostream &os, const string &s, char c) {
    return os << s << c;
}
for_each(words.begin(), words.end(), bind(print, ref(os), _1, ' '));  // 通过bind传递一元谓词，代替对应的lambda。
```

# 类

每个**类（class）**定义了唯一的类型，被称为**类类型（class type）**。即使两个类的成员完全一样，这两个类也是不同的类型。

## 定义

### 类的定义

类是一种抽象数据类型，其定义如下：

```c++
class_or_struct Class {
    body
};
```

其中```class_or_struct```为```class```或```struct```。```Class```为类名，```body```为类的主体。类的主体中可以包含**数据成员（data member）**、**成员函数（member function）**与其他**成员（member）**（如类型别名等实体）。

```c++
// 书店程序。

struct Sales_data {
    string isbn() const {
        return bookNo;
    }
    Sales_data &combine(const Sales_data&);
    double avg_price() const;
    string bookNo;
    unsigned units_sold = 0;
    double revenue = 0.0;
};

Sales_data add(const Sales_data&, const Sales_data&);
ostream &print(ostream&, const Sales_data&);
istream &read(istream&, Sales_data&);
```

```c++
// Sales_data的一种典型使用。
Sales_data total;
if (read(cin, total)) {
    Sales_data trans;
    while(read(cin, trans)) {
        if (total.isbn() == trans.isbn()) {
            total.combine(trans);
        }
        else {
            print(cout, total) << endl;
            total = trans;
        }
    }
    print(cout, total) << endl;
} else {
    cerr << "No data?!" << endl;
}
```

```c++
// 表示屏幕（或显示器的窗口）的类。

class Screen {
public:
    typedef string::size_type pos;
private:
    pos cursor = 0;  // 光标的位置。
    pos height = 0, width = 0;  // 屏幕的高与宽。
    string contents;  // 保存屏幕的内容。
};
```

### 类的声明

类的声明为：```class Class;```。其中```Class```为类名。这种声明有时也被称为**前向声明（forward declaration）**。

类被声明之后定义之前为**不完全类型（incomplete type）**。

不完全类型的成员未知、存储空间未知，因此使用引用或指针访问其成员，可以定义指向不完全类型的指针或引用，可以声明但不能定义以不完全类型作为参数或返回类型的函数，可以声明但不能定义不完全类型的对象。

```c++
class Link_screen {
    Screen window;
    Link_screen *next;  // 正确。不能是Link_screen。
    Link_screen *prev;  // 正确。
};
```

### 对象的定义

类类型变量一般被称为对象。对象的实例化方式有两种：

- ```Class object```。其中```Class```为类名，```object```为对象名。对象调用类的默认构造函数完成默认初始化。注意不能写成```Class object()```，否则这就声明了一个函数了。
- ```Class object(args)```。其中```Class```为类名，```object```为对象名，```args```为参数列表，不能为空。对象调用类的对应的非默认构造函数完成初始化，其中```args```参数列表用于初始化该非函数的形参列表。

每个对象都有自己的非静态数据成员或函数，即同一类的不同对象的非静态数据成员或函数互不干扰。同一类的不同对象的其他成员属于类本身，为这些对象所共享。

对象定义时，可以前置```class```或```struct```，该方式从C语言继承而来。

## 指针

### ```this```

非静态成员函数通过名为```this```的隐式指针参数来访问其调用的对象。当通过```object.member(parameter_list)```调用```object```对象的```member```成员函数时，可认为编译器将其重写为：```Class::member(&object, parameter_list)```（当然这只是伪代码），其中```Class```为```object```所属的类，```object```的地址被隐式传递给```this```。

```this```是常量指针，且默认情况下指向非常量对象，即其类型为```Class *const```。

在类内或类的作用域内可以直接访问成员函数的任何成员而无需通过成员访问运算符，任何对类（非静态）成员的直接访问都被看作```this```的隐式引用。

```c++
string isbn() const {  // Sales_data类的成员。
    return this -> bookNo;  // 另一种定义isbn的方式，但没有必要。
}
```

```c++
// Sales_data的combine成员的定义。
// 返回*this。
Sales_data &Sales_data::combine(const Sales_data &rhs) {  // 注意返回左侧运算对象的引用，以与内置赋值运算符保持统一。
    units_sold += rhs.units_sold;
    revenue += rhs.revenue;
    return *this;  // 返回对象本身。
}
```

```c++
// Screen的set成员。
// move与set成员都是const成员函数，返回（非const）*this（引用）。

class Screen {
public:
    Screen &set(char);
    Screen &set(pos, pos, char);
    // ...
};
inline Screen &Screen::set(char c) {
    contents[cursor] = c;
    return *this;
}
inline Screen &Screen::set(pos r, pos col, char ch) {
    contents[r * width + col] = ch;
    return *this;
}

Screen myScreen(5, 3);
myScreen.move(4, 0).set('#');  // 级联调用。注意move必须返回返回（非const*）的this（引用）。
```

### 类成员指针

**成员指针（pointer to member）**是指可以指向类（而不是对象）的非静态成员的指针。类的静态成员不属于任何对象，因此不需要特殊的指向静态成员的指针：指向静态成员的指针就是普通指针。

成员指针的类型包括了类的类型与类成员的类型。当初始化该指针时，我们令其指向类的某个成员，但是不指定该成员所属的对象，直到使用成员指针时，才提供成员所属的对象。

```c++
// 一个用作示例的Screen类。
class Screen {
public:
    typedef string::size_type pos;
    char get_cursor() const {
        return contents[cursor];
    }
    char get() const;
    char get(pos ht, pos wd) const;
private:
    string contents;
    pos cursor;
    pos height, width;
};
```

#### 数据成员指针

```c++
// 定义并初始化数据成员指针。
// 对于pdata的使用必须位于Screen类的成员或友元内。
const string Screen::*pdata;  // pdata为可以指向（const或非const的）Screen对象的string成员。
pdata = &Screen::contents;
auto pdata = &Screen::contents;  // 另一种写法：使用auto（或者decltype）自动推断成员指针类型。
```

```c++
// 使用数据成员指针。
// 对于pdata的使用必须位于Screen类的成员或友元内。
Screen myScreen, *pScreen = &myScreen;
auto s = myScreen.*pdata;
s = pScrren ->* pdata;
```

> Conceptually, these operators perform two actions: They dereference the pointer to member to get the member that we want; then, like the member access operators, they fetch that member from an object (```.*```) or through a pointer (```->*```).

常规的访问控制规则对成员指针同样有效。

```c++
// 因为数据成员通常是私有的，通常不能直接获取数据成员的指针。
// 这里为Screen定义一个返回其成员指针的函数。
class Screen {
    static const string Screen::*data() {
        return &Screen::contents;
    }
    // ...
};

const string Screen::*pdata = Screen::data();  // 定义并初始化成员函数指针
auto s = myScreen.*pdata;  // 另一种写法：使用auto（或者decltype）自动推断成员指针类型。
```

#### 成员函数指针

```c++
// 定义并初始化成员函数指针。

auto pmf = &Screen::get_cursor;  // 使用auto自动推断类型。

// 显式声明函数类型。
// 如果成员被重载，则必须显式声明类型。
// 不能写成：char Screen::*p(Screen::pos, Screen::pos) const（该声明试图定义一个名为p的普通函数，其返回一个指向Screen类的类型为char的成员的指针，因此不能使用const限定符）。
char (Screen::*pmf2)(Screen::pos, Screen::pos) const;
pmf2 = &Screen::get;
```

在成员函数与指向该成员的指针之间不存在自动转换。

```c++
pmf = &Screen::get;  // 正确。
pmf = Screen::get;  // 错误。
```

```c++
// 使用成员函数指针。

Screen myScreen, *pScreen = &myScreen;
// 不能去掉两个语句的第一个括号。
// 例如：myScreen.pmf()含义为myScreen.*(pmf())，因为pmf不是函数，错误。
char c1 = (pScreen ->* pmf)();
char c2 = (myScreen.*pmf2)(0, 0);
```

```c++
// 使用类型别名。
using Action = char (Screen::*)(Screen::pos, Screen::pos) const;
Action get = &Screen::get;

Screen &action(Screen&, Action=&Screen::get);  // 将成员函数指针作为函数的形参类型并指定默认实参（当然也可以作为函数的返回类型）。

Screen myScreen;
action(myScreen);
action(myScreen, get);
action(myScreen, &Screen::get);
```

```c++
// 成员指针函数表。
// 如果一个类含有几个相同类型的成员，则这样的一个表可以帮助我们从这些成员中选择一个。

// 为Screen类定义几个负责向指定方向移动光标的函数。
class Screen {
    // ...
    Screen &home();
    Screen &forward();
    Screen &back();
    Screen &up();
    Screen &down();
};

// 定义一个move函数，可以调用以上任意一个函数并执行对应的操作。
// 为了实现move函数，需要实现一些辅助操作。
class Screen {
public:
    // ...
    using Action = Screen &(Screen::*)();
    enum Directions { HOME, FORWARD, BACK, UP, DOWN };
    Screen &move(Directions);
private:
    // ...
    static Action Menu[];
};

Screen &Screen::move(Directions cm) {
    return (this ->* Menu[cm])();
}

Screen::Action Screen::Menu[] = {
    &Screen::home,
    &Screen::forward,
    &Screen::back,
    &Screen::up,
    &Screen::down,
};

// 使用演示。
Screen myScreen;
myScreen.move(Screen::HOME);
myScreen.move(Screen::DOWN);
```

#### 将成员函数作为可调用对象

与普通的函数指针不同，成员指针不是可调用对象，不支持函数调用运算符。

```c++
auto fp = &string::empty;
find_if(svec.begin(), svec.end(), fp);  // svec为vector<string>。错误，必须使用.*或->*调用成员指针，find_if内部执行类似于如下的语句：if (fp(*it))，而该语句是错误的。
```

可以使用```function```为成员函数生成可调用对象。

```c++
function<bool (const string&)> fcn = &string::empty;
find_if(svec.begin(), svec.end(), fcn);  // 可以认为find_if中有类似于如下的语句：if (fp(*it))，本质上function类将该语句转换为：if (((*it).*p)())。
```

```c++
vector<string*> pvec;
function<bool (const string*)> fp = &string::empty;
find_if(pvec.begin(), pvec.end(), fp);
```

可以使用```mem_fn```为成员函数生成可调用对象。```mem_fn```定义在头文件```functional```中，其可以从成员指针生成可调用对象。与```function```不同的是，```mem_fn```可以根据成员指针的类型推断可调用对象的类型。

```c++
find_if(svec.begin(), svec.end(), mem_fn(&string::empty));
```

```mem_fn```生成的可调用对象可以通过对象或指针调用。

```c++
auto f = mem_fn(&string::empty);
f(*svec.begin());  // f使用.*调用empty。
f(&svec[0]);  // f使用.->调用empty。
```

也可以使用```bind```为成员函数生成可调用对象。

```c++
auto it = find_if(svec.begin(), svec.end(), bind(&string::empty, _1));
```

```c++
auto f = bind(&string::empty, _1);
// 与mem_fn类似，bind生成的可调用对象的第一个实参可以是指向string的指针或引用。
f(*svec.begin());  // f使用.*调用empty。
f(&svec[0]);  // f使用.->调用empty。
```

## 组成

类的对象使用对象的成员访问运算符访问其（静态或非静态）数据成员；类使用类的作用域运算符访问其静态成员或其他非数据或函数的成员。

### 数据成员

数据成员即为类所有的变量（包括类）。

非静态数据成员必须定义在类中。

类对象的非静态数据成员的初始化顺序与其在类定义中出现的顺序一致。

在定义数据成员时，可以提供**类内初始值（in-class initializer）**，其将用于初始化数据成员。类内初始值必须使用```=```或花括号初始化的形式，不允许使用括号初始化。

```c++
// 窗口管理类，表示Screen集合。
class Window_mgr {
private:
    vector<Screen> screens{Screen(24, 80, ' ')};
};
```

#### 可变数据成员

**可变数据成员（mutable data member）**永远不是```const```，即使其为```const```对象的成员，```const```成员函数可以改变可变数据成员的值。

在（非静态）数据成员定义时前置```mutable```使之成为可变数据成员。

```c++
// 通过access_ctr追踪Screen的some_member成员函数被调用多少次。

class Screen {
public:
    void some_member() const;
    // ...
private:
    mutable size_t access_ctr;
    // ...
};

void Screen::some_member() const {
    ++access_ctr;
}
```

### 成员函数

类的成员函数也被称为**方法（method）**。

成员函数必须在类中声明，但可以定义在类外，此时必须使用作用域运算符作用于成员函数指出成员函数所属的类，并使得成员函数的参数列表与函数体均位于类的作用域内。不允许在类内重复声明同一个成员函数，类内也无法声明或定义非成员函数。

```c++
// 类外定义成员函数
return_type Class::name(parameter_list) {  // 该函数为Class的成员函数，必须在类中声明。
    // ...
};
```

成员函数可以为内联函数。可以在类内声明时或类外定义时将成员函数设置为内联函数。类内定义的函数是隐式内联的，与在头文件中定义普通内联函数的原因一样，内联成员函数也应该与相应的类定义在同一个头文件中。

成员函数也可以重载，其重载规则同非成员函数。

```c++
// Screen的get与move成员。

class Screen {
public:
    char get() const {  // 默认为内联函数。
        return contents[cursor];
    }
    inline char get(pos ht, pos wd) const {  // 显式inline。
        return contents[cursor];
    }
    Screen &move(pos r, pos c);
    // ...
}

inline Screen &Screen::move(pos r, pos c) {
    pos row = r * width;
    cursor = row + c;
    return *this;
}

char Screen::get(pos r, pos c) const {
    pos row = r * width;
    return contents[row + c];
}

Screen myscreen;
char ch = myscreen.get();  // 调用Screen::get()。
ch = myscreen.get(0, 0);  // 调用Screen::get(pos, pos)。
```

#### ```const```成员函数

通过对（非静态）成员函数的形参列表后置```const```关键字可使得该成员函数成为常量成员函数（const member function）。常量成员函数不能改变其所属对象的内容。常量对象、常量对象的引用或指针只能调用常量成员函数，因为根据底层```const```初始化或赋值规则，只能将指向常量对象的```this```传递给常量成员函数。

如果```const```成员函数以引用的形式返回```*this```，则其返回类型为常量引用，即使使用非常量对象调用之。

```c++
// Sales_data的avg_price成员的定义。
// 这是一个const成员函数。
double Sales_data::avg_price() const {
    if (units_sold)
        return revenue / units_sold;
    else
        return 0;
}
```

通过区分成员函数是否是```const```的，可以对其重载，其原理类似于基于底层```const```参数进行重载。

```c++
// Screen的display成员。

class Screen {
public:
    Screen &display(ostream &os) {
        do_display(os);
        return *this;
    }
    const Screen &display(ostream &os) const {  // 注意其返回const Screen&。
        do_display(os);
        return *this;
    }
    // ...
private:
    void do_display(ostream &os) const {
        os << contents;
    }
    // ...
};

Screen myScreen(5, 3);
const Screen blank(5, 3);
myScreen.set('#').display(cout);  // 调用非const版本的display，可以对display再进行级联调用。
blank.display(cout);  // 调用const版本的display，无法对display再进行级联调用。
```

#### 引用与成员函数

一个成员函数（非构造函数与赋值运算符）可以同时提供拷贝与移动版本并从中受益——一个版本接受```const```左值引用，一个版本接受非```const```的右值引用，这与拷贝/移动构造函数与赋值运算符的形参模式相同。例如，定义了```push_back```的标准库容器提供了两个```push_back```版本：```void push_back(const X&)```与```void push_back(X&&)```（假设元素类型为```X```）。

> Member functions other than constructors and assignment can benefit from providing both copy and move versions. Such move-enabled members typically use the same parameter pattern as the copy/move constructor and the assignment operators—one version takes an lvalue reference to ```const```, and the second takes an rvalue reference to non```const```.

一般来说，不需要为函数定义接受```const X&&```（通常，我们希望接受右值引用的版本“窃取”实参数据）或```X&```（从一个对象进行拷贝不应该改变被拷贝的对象）的版本。

```c++
// 为StrVec类重新定义push_back，包含两个版本。
class StrVec {
public:
    void push_back(const string&);
    void push_back(string&&);
    // ...
};

void StrVec::push_back(const string &s) {
    chk_n_alloc();
    alloc.construct(first_free++, s);
}
void StrVec::push_back(string &&s) {
    chk_n_alloc();
    alloc.construct(first_free++, std::move(s));
}
```

```c++
StrVec vec;
string s = "some string or another";
vec.push_back(s);  // 调用push_back(const string&)。
vec.push_back("done");  // 调用push_back(string&&)。
```

通常，我们在对象上调用成员函数，不管该对象是左值还是右值。

```c++
string s1 = "a value", s2 = "another";
auto n = (s1 + s2).find('a');

s1 + s2 = "wow!";  // 一个令人惊讶的右值使用方式。
```

通过对（非静态）成员函数的形参列表后置一个**引用限定符（reference qualifier）**（```&```或```&&```）来指出```this```可以指向左值或右值。类似定义```const```成员函数的```const```限定符，引用限定符只能出现在非静态成员函数中，且必须同时出现在函数声明与定义中。

```&```限定的函数只能用于左值，```&&```限定的函数只能用于右值。

```c++
class Foo {
    Foo &operator=(const Foo&) &;
    // ...
};
Foo &Foo::operator=(const Foo &rhs) & {
    // ...
    return *this;
}

Foo &retFoo();
Foo retVal();
Foo i, j;
i = j;  // 正确，i与j都是左值。
retFoo() = j;  // 正确，retFoo()返回一个左值。
retVal() = j;  // 错误，retVal()返回一个右值。
i = retVal();  // 正确，可以将右值作为赋值操作的右侧运算对象。
```

一个函数可以同时被```const```与引用限定，此时引用限定符必须跟随在```const```限定符后。

类似于基于```const```限定符的重载，通过区分成员函数的引用限定符，可以对成员函数进行重载。当然，进一步地，可以同时根据```const```与引用限定符进行重载。

```c++
class Foo {
public:
    Foo sorted() &&;  // 运行于可修改的右值。
    Foo sorted() const &;  // 运行于任意类型的Foo。
    // ...
private:
    vector<int> data;
};

// 本对象为右值，没有其他用户，可以原址排序。
Foo Foo::sorted() && {
    sort(data.begin(), data.end());
    return *this;
}

// 本对象可能为const的或左值，哪种情况都不能原址排序。
Foo Foo::sorted() const & {
    Foo ret(*this);
    sort(ret.data.begin(), ret.data.end());
    return ret;
}

retVal().sorted();  // retVal()为右值，调用Foo::sorted()&&。
retFoo().sorted();  // retFoo()为左值，调用Foo::sorted() const &。
```

重载的成员函数（相同名称与形参列表）必须同时有引用限定符，或同时没有引用限定符。

```c++
class Foo {
public:
    Foo sorted() &&;
    Foo sorted() const;  // 错误。
    using Comp = bool(const int&, const int&);
    Foo sorted(Comp*);  // 正确，形参列表不同。
    Foo sorted(Comp*) const;  // 正确，两个版本都没有引用限定符。
};
```

### 静态成员

类的静态成员与类本身相关。通过对数据成员或成员函数声明前置```static```使其成为静态成员。在类的作用域外，通过类作用域运算符来访问类的静态成员，也可以像非静态成员那样访问静态成员（通过类的对象、引用或指针）。```static```关键字只能出现在类内声明或定义处，类外不能重复```static```关键字。

```c++
// 银行的账户记录类。
// 对象只有两个数据成员：owner与amount。
class Account {
public:
    void calculate() {
        amount += amount * interestRate;  // 成员函数无需使用作用域运算符访问其静态成员。
    }
    static double rate() {
        return interestRate;
    }
    static void rate(double);
private:
    string owner;
    double amount;
    static double interestRate;  // 利率为静态成员，不属于对象。
    static double initRate();
};

double r;
r = Account::rate();  // 使用类的作用域运算符访问静态成员。

Account ac11;
Account *ac2 = &ac1;
r = ac1.rate();  // 使用对象或引用访问静态成员。
r = ac2 -> rate();  // 使用指针访问静态成员。
```

类的静态成员独立于任何对象外，对象中不包含任何与静态数据成员有关的数据。静态成员也不与任何对象绑定在一起，不包含```this```指针，因此静态成员函数不能是```const```的，也不能在静态成员函数体内（显式或隐式）使用```this```指针，如引用非静态的成员。

在某些无法使用非静态成员的场合可以使用静态成员。

- 静态数据成员可以是不完全类型，特别地，静态数据成员的类型可以为其所属的类类型，而非静态数据成员只能声明为其所属类的指针或引用。

```c++
class Bar {
public:
    // ...
private:
    static Bar mem1;  // 静态成员可以为不完全类型。
    Bar *mem2;  // 指针成员可以为不完全类型。
    Bar mem3;  // 错误。
}
```

- 静态成员可以作为默认实参，普通成员由于值属于对象的一部分，因此不能作为默认实参。

```c++
class Screen {
public:
    Screen &clear(char=bkground);
private:
    static const char bkground;
}
```

#### 静态数据成员

因为静态数据成员不属于类的任何一个对象，静态数据成员不是在创建类的对象时被定义的，也不是在类的构造器中被初始化的。并且一般来说，不能在类的内部初始化静态成员，必须在类的外部定义与初始化每个静态成员。一个静态成员也只能被定义一次。

类似全局变量，静态数据成员定义在任何函数之外，因此一旦被定义就存在于程序的整个生命周期中。

```c++
double Account::interestRate = initRate();
```

如果静态数据成员是```const```整型，则可以为其提供类内初始值；如果静态数据成员是```constexpr```字面值类型，则必须提供类内初始值。初始值必须是常量表达式。这样的成员本身是常量表达式。

```c++
class Account {
    // ...
private:
    static constexpr int period = 30;
    double daily_tbl[period];
}
```

如果静态数据成员类内已提供初始值，则类外定义时不能再提供初始值。

如果一个静态数据成员仅仅用在编译器可以替换其值得情况下，则一个初始化的```const```或```constexpr static```无需分别定义，否则该成员必须有一条（类外）定义语句。

```c++
constexpr int Account::period;  // 如果period还用于其他地方，例如传递给接受const int&的函数时，则必须类外定义period，此时不要指定初始值。
```

#### 静态成员函数

静态成员函数同样可以在类内定义，或类内声明类外定义。

```c++
void Account::rate(double newRate) {  // 无static关键字。
    interestRate = newRate;
}
```

### 类相关的非成员函数

如果一个函数在概念上属于类（即用于操作类），则其一般应与类本身声明于同一头文件中。

```c++
// Sales_data相关的非成员函数的定义。

istream &read(istream &is, Sales_data &item) {
    double price = 0;
    is >> item.bookNo >> item.units_sold >> price;
    item.revenue = price * item.units_sold;
    return is;
}

ostream &print(ostream &os, const Sales_data &item) {
    os << item.isbn() << " " << item.units_sold << " " << item.revenue << " " << item.avg_price();
    return os;
}

Sales_data add(const Sales_data &lhs, const Sales_data &rhs) {
    Sales_data sum = lhs;
    sum.combine(rhs);
    return sum;  // 注意返回副本（右值），以与内置加法运算符保持统一。
}
```

### 其他成员

类可以有其他成员，如类型别名。

```c++
class Screen {
    typedef string::size_type pos;
    // ...
};
```

## 类的作用域

每个类定义自己的作用域。在类的作用域外，普通数据成员与成员函数只能由对象、引用或指针使用使用成员访问运算符来访问，静态成员或类型成员则使用作用域运算符访问，静态成员也可以由对象、引用或指针使用使用成员访问运算符来访问。

```c++
Screen::pos ht = 24, wd = 80;  // 使用作用域运算符访问类型。
Screen scr(ht, wd, ' ');
Screen *p = &scr;
char c = scr.get();  // 由对象使用成员访问运算符来访问普通成员函数。
c = p->get();  // 由指针使用成员访问运算符来访问普通成员函数。
```

```c++
void Window_mgr::clear(ScreenIndex i) {  // 对clear使用类作用域运算符，使得定义的剩余部分在类的作用域内。
    Screen &s = screens[i];
    s.contents = string(s.height * s.width, ' ');
}
```

```c++
class Window_mgr {
public:
    ScreenIndex addScreen(const Screen&);  // 为Window_mgr添加该成员，负责添加一个新的Screen。
    // ...
};
Window_mgr::ScreenIndex Window_mgr::addScreen(const Screen &s) {  // 这里必须同时为返回类型与函数名提供类作用域运算符。
    screens.push_back(s);
    return screens.size() - 1;
}
```

### 名字查找

类的定义分为两步：首先编译类的成员声明；直到类全部可见后才编译函数体。因此类的成员函数函数体可以使用类中定义的任何名字，但是成员函数的声明中使用的名字必须在使用前已出现。如果某个成员的声明使用了类中尚未出现的名字，则在类定义所在的作用域内继续查找该名字。

```c++
typedef double Money;
string bal;
class Account {
public:
    Money balance() {  // 这里的Money是外层作用域中的Money，即double。
        return bal;  // 数据成员bal。
    }
private:
    Money bal;
    // ...
};
```

如果一个类的成员使用了外层作用域中的一个名字，且该名字代表一种类型，则类不能随后重定义该名字。

```c++
typedef double Money;
class Account {
public:
    Money balance() {
        return bal;
    }
private:
    typedef double Money;  // 错误，不能重定义Money。一些编译器对此不报错。
    Money bal;
};
```

成员函数函数体内使用的名字按照如下方式解析：

- 首先，在成员函数内查找该名字的声明。只有在名字使用前出现的声明才被考虑。

- 如果成员函数内没有找到声明，则在类内继续查找，此时类的所有成员都被考虑。

- 如果类内没有找到声明，则在成员函数定义前的作用域内继续查找声明。

  > If a declaration for the name is not found in the class, look for a declaration that is in scope before the member function definition.

```c++
// 仅作为示例，不建议使用其他成员的名字作为某个成员函数的形参名。
int height;
class Screen {
public:
    typedef string::size_type pos;
    void dummy_fcn(pos height) {
        cursor = width * height;  // height为函数形参。
    }
private:
    pos cursor = 0;
    pos height = 0, width = 0;
};

// 重写Screen::dummy_fcn。
// 仅作为示例，成员函数中的名字不应该隐藏成员名。
void Screen::dummy_fcn(pos height) {
    cursor = width * this->height;
    cursor = width * Screen::height;  // 现在height是Screen的一个数据成员。
}

// 重写Screen::dummy_fcn。
// 建议的写法：不要将成员名作为形参名或其他局部变量名。
void Screen::dummy_fcn(pos ht) {
    cursor = width * height;  // height是Screen的一个数据成员。
}
```

```c++
int height;
class Screen {
public:
    typedef string::size_type pos;
    void setHeight(pos);
    pos height = 0;  // 隐藏外层作用域中的height。
};
Screen::pos verify(Screen::pos);
void Screen::setHeight(pos var) {
    /*
     * var是形参。
     * height是类的成员。
     * verify是全局函数。
     */
    height = verify(var);
}
```

## 拷贝控制

类定义了**构造函数（constructor）**用于控制类的初始化过程，同时定义了5类特殊的成员函数来控制对象的拷贝、移动、赋值与销毁时的行为。这5类函数是：**拷贝构造函数（copy constructor）**、**拷贝赋值运算符（copy-assignment operator）**、**移动构造函数（move constructor）**、**移动赋值运算符（move-assignment operator）**与**析构函数（destructor）**，这5种操作称为**拷贝控制（copy control）**。

### 构造函数

构造函数用于初始化类的对象（的数据成员），只要类的对象被创建就会执行构造函数。构造函数是特殊的函数，其函数名与类名相同，没有返回类型（不能指定返回类型，可以有```return;```）。

构造函数不能被声明为```const```的，因为当构造函数被调用时，对象还未被完全初始化，还未成为常量。

构造函数同样可以在类内声明并在类外定义。

#### 默认构造函数

没有参数的构造函数被称为**默认构造函数（default constructor）**，默认构造函数用来完成默认初始化。

##### 合成的默认构造函数

当类没有定义任何构造函数时，则编译器会为其生成一个默认构造函数，其被称为合成的**默认构造函数（synthesized default constructor）**。

合成的默认构造函数只适用于比较简单的类，对于一般的类，最好显式定义默认构造函数，原因包括：

- 如果类定义了其他构造函数且未显式定义默认构造函数，则类将没有默认构造函数。
- 合成的默认构造函数可能执行错误的操作。如果类的数据成员未被构造器初始化或未使用类内初始值初始化，则其值可能未定义。
- 某些情况下编译器不能为类合成默认的构造函数（或将其声明为```=delete```的）。例如：如果类中包含一个其他类类型的成员且其无默认构造函数，则编译器无法初始化该成员。

```c++
class NoDefault {
public:
    NoDefault(const string&);
};

struct A {
    NoDefault my_mem;
}

A a;  // 错误，不能为A合成构造函数。

struct B {
    B() {}  // 错误，B的b_member成员没有初始值。
    NoDefault b_member;
}
```

#### 构造函数初始值列表

**构造函数初始值列表（constructor initialize list）**负责初始化对象的数据成员。构造函数初始值列表为逗号分隔的名字列表，每个名字后面紧跟括号或花括号括起来的成员初始值。

```c++
// 为Sales_data设计4个构造函数。
struct Sales_data {
    Sales_data() = default;
    Sales_data(const string &s): bookNo(s) {}
    Sales_data(const string &s, unsigned n, double p): bookNo(s), units_sold(n), revenue(p * n) {}
    Sales_data(istream&);
    // ...
}
```

```c++
// 重新设计Sales_data的构造函数，使用默认实参。
class Sales_data {
public:
    Sales_data(string s = ""): bookNo(s) {}
    Sales_data(string s, unsigned cnt, double rev): bookNo(s), units_sold(cnt), revenue(rev*cnt) {}
    Sales_data(istream &is) {
        read(is, *this);
    }
    // ...
};
```

构造函数初始值列表不应覆盖类内初始值，除非新赋的值与原值不同。如果类不能提供类内初始值，则每个构造函数应显式初始化每个内置类型的成员。

当构造函数执行到函数体时，数据成员的初始化就结束了。在构造函数体内无法初始化数据成员，只能为其赋值。如果类中含有```const```成员、引用成员或未提供默认构造函数的类类型的成员，且未提供类内初始值，则必须在构造函数初始值列表中初始化。

```c++
class ConstRef {
public:
    ConstRef(int ii);
private:
    int i;
    const int ci;
    int &ri;
};

// 错误的构造函数。
ConstRef::ConstRef(int ii) {
    i = ii;
    ci = ii;  // 错误。
    ri = i;  // 错误。
}

// 正确的构造函数。
ConstRef::ConstRef(int ii): i(ii), ci(ii), ri(i) {}
```

因为数据成员的初始化顺序与其在类中定义的顺序一致，最好令构造函数初始值顺序与成员声明顺序一致，且尽量避免使用一个数据成员初始化其他数据成员。

```c++
class X {
    int i;
    int j;
public:
    X(int val): j(val), i(j) {}  // 错误，i先被初始化。
};
```

当某个数据成员被构造函数初始值列表忽略时，它将以与合成默认构造函数相同的方式被隐式初始化。

```c++
// Sales_data的接受一个string参数的构造函数等价于：
Sales_data(const string &s): bookNo(s), units_sold(0), revenue(0) {}
```

```c++
// Sales_data的接受一个istream&参数的构造函数的定义。
Sales_data::Sales_data(istream &is) {
    read(is, *this);
}
```

#### 委托构造函数

一个**委托构造函数（delegating constructor）**调用所属类的其他构造函数执行其初始化过程。委托构造函数同样有一个成员初始值列表与函数体。其中，成员初始值列表只能包含对所属类其他构造函数的调用。

当一个构造函数委托给另一个构造函数时，受委托的构造函数的初始值列表与函数体依次被执行，然后再将控制权交换给委托者的函数体。

```c++
// 将Sales_data的部分构造函数委托给其他构造函数。
class Sales_data {
    Sales_data(string s,unsigned cnt, double price): bookNo(s), units_sold(cnt), revenue(cnt * price) {}
    Sales_data(): Sales_data("", 0, 0) {}  // 委托给第1个构造函数。
    Sales_data(string s): Sales_data(s, 0, 0) {}  // 委托给第1个构造函数。
    Sales_data(istream &is): Sales(data()) {  // 委托给第2个构造函数。
        read(is, *this);
    }
}
```

#### 转换构造函数

如果一个类的构造函数只接受一个实参，则可通过该构造函数将该实参类型自动转换为该类类型。这被称为**转换构造函数（converting constructor）**。

```c++
string null_book = "9-999-99999-9";
item.combine(null_book);  // item为Sales_data类型，null_book自动转换为Sales_data类型的临时变量。此时要求combine的参数为常量引用。
```

编译器只会自动执行一步用户定义的类型转化（内置类型转换可以置于其前或其后）。

```c++
item.combine("9-999-99999-9");  // 错误，需要先将参数转换为string，然后转换为Sales_data。
item.combine(string("9-999-99999-9"));  // 正确。
item.combine(Sales_data("9-999-99999-9"));  // 正确。
```

通过对构造函数前置```explicit```可以阻止这种隐式转换。```explicit```只能用于类内声明处，不能用于类外定义处。

```c++
class Sales_data {
    explicit Sales_data(const string &s): bookNo(s) {}
    explicit Sales_data(istream&);
};

item.combine(null_book);  // 错误。
item.combine(cin);  // 错误。
```

```explicit```构造函数阻止拷贝初始化过程，只能直接初始化，编译器不会在自动转换中使用该构造函数。

```c++
Sales_data item(null_book);  // 正确。
Sales_data item2 = null_book;  // 错误。
```

可以显式转换```explicit```构造函数。

```c++
item.combine(Sales_data(null_book));
item.combine(static_cast<Sales_data>(cin));
```

标准库中，接受单参数```const char*```的```string```构造函数不是```explicit```的，而接受容量参数的```vector```构造函数是```explicit```的。

### 拷贝构造函数

如果一个构造函数的第一个形参是自身类类型的（左值）引用，并且任何其他的形参都有默认值，则该构造函数是拷贝构造函数。

> A constructor is the copy constructor if its first parameter is a reference to the class type and any additional parameters have default values:

如果拷贝构造函数的第一个形参不是引用，则调用拷贝构造函数需要先拷贝其实参，但为了拷贝实参，又需要调用拷贝构造函数，导致无限循环。

拷贝构造函数的第一个形参几乎总是一个常量引用，因为拷贝构造函数一般不会改变被拷贝的对象。

```c++
class Foo {
public:
    Foo();  // 默认构造函数。
    Foo(const Foo&);  // 拷贝构造函数。
    // ...
}
```

#### 拷贝初始化与直接初始化

当使用```=```初始化变量时，我们请求编译器**直接初始化（direct initialization）**该对象，将等号右侧的初始值（initializer）拷贝到新创建的对象中。当使用直接初始化时，实际上是要求编译器使用普通的函数匹配来选择与提供的实参最配的构造函数。

如果初始化忽略```=```，则使用**拷贝初始化（copy initialization）**，当使用拷贝初始化时，实际上是要求编译器将右侧运算对象拷贝到正在创建的对象中，如果需要的话还要进行类型转换。

当使用多个值初始化一个变量时，必须使用直接初始化。

```c++
string dots(10, '.');  // 直接初始化。
string s(dots);  // 直接初始化。
string s2 = dots;  // 拷贝初始化。
string null_book = "9-999-99999-9";  // 拷贝初始化。
string nines = string(100, '9');  // 拷贝初始化。
```

拷贝初始化通常使用拷贝构造函数完成（也有可能通过移动构造函数完成）。

拷贝初始化不仅发生在使用```=```定义变量时，也会发生在以下情况：

- 将一个对象作为实参传递给一个非引用类型的形参。
- 从一个返回类型为非引用类型的函数返回一个对象。
- 用花括号列表初始化一个数组中的元素或一个聚合类中的成员。
- 某些类类型对其分配的对象使用拷贝初始化。如初始化标准库容器或调用```insert```或```push```函数时，容器会对元素拷贝初始化。与之相对，使用```emplace```成员创建的元素都进行直接初始化。

如果使用的初始化要求通过一个```explicit```的构造函数进行类型转换，则使用拷贝初始化还是直接初始化就不是无关紧要的了。

```c++
vector<int> v1(10);  // 正确，直接初始化。
vector<int> v2 = 10;  // 错误，接受大小形参的构造函数是explicit的。
void f(vector<int>);  // f的形参进行拷贝初始化。
f(10);  // 错误。
f(vector<int>(10));  // 正确。
```

在拷贝初始化过程中，编译器可以跳过拷贝/移动构造函数，直接创建对象，但是此时拷贝/移动构造函数必须是存在且可访问的。

```c++
string null_book = "9-999-99999-9";  // 拷贝初始化，允许被编译器改写为：string null_book("9-999-99999-9")，跳过了拷贝构造函数，但是此时拷贝构造函数必须可访问。
```

#### 合成的拷贝构造函数

如果一个类没有显式定义拷贝构造函数，则编译器会为其生成一个**合成的拷贝构造函数（synthesized copy constructor）**。某些情况下，合成的拷贝构造函数可能阻止我们拷贝该类类型的对象，否则，合成的拷贝构造函数会将其实参的成员逐个（memberwise copy）拷贝到被创建的对象中，编译器将给定对象中的每个非```static```成员拷贝到正在创建的对象中。

每个成员的类型决定了它被如何拷贝：类类型成员通过该类的拷贝构造函数被拷贝；内置类型成员则被直接拷贝；数组不能被直接拷贝，但合成的拷贝构造函数会逐个拷贝数组元素，如果数组元素是类类型，则使用元素的拷贝构造函数来拷贝。

```c++
class Sales_data {
public:
    // ...
    // 等价于合成的拷贝构造函数。
    Sales_data(const Sales_data&);
private:
    string bookNo;
    int units_sold = 0;
    double revenue = 0.0;
};

Sales_data::Sales_data(const Sales_data &orig): bookNo(orig.bookNo), units_sold(orig.units_sold), revenue(orig.revenue) {}
```

### 拷贝赋值运算符

类的拷贝赋值运算符为重载的赋值运算符，其接受一个实参（通常为常量引用），该实参的类型与该类的类型相同。为了与内置类型的赋值保持一致，拷贝赋值运算符通常返回左侧运算对象的（左值）引用。

```c++
class Foo {
public:
    Foo &operator=(const Foo&);  // 拷贝赋值运算符。
    // ...
}
```

#### 合成的拷贝赋值运算符

如果一个类没有显式定义拷贝赋值运算符，则编译器会为其生成一个**合成的拷贝赋值运算符（synthesized copy-assignment operator）**。某些情况下，合成的拷贝赋值运算符可能阻止我们进行赋值操作，否则，合成的拷贝赋值运算符会使用成员的拷贝赋值运算符将右侧运算对象的每个非```static```成员赋值给左侧运算对象的相应成员。如果成员为数组，则逐个赋值数组元素。合成的拷贝赋值运算符返回左侧运算对象的引用。

```c++
// 等价于合成的拷贝赋值运算符。
Sales_data &Sales_data::operator=(const Sales_data &rhs) {
    bookNo = rhs.bookNo;
    units_sold = rhs.units_sold;
    revenue = rhs.revenue;
    return *this;
}
```

### 析构函数

**析构函数（destructor）**执行与构造函数相反的操作：释放对象使用的资源并销毁对象的非静态数据成员。

析构函数是类的成员函数，名字由```~```紧接类名组成。它没有返回值，也不接受形参，但是有一个函数体。

```c++
class Foo {
public:
    ~Foo();  // 析构函数。
    // ...
}
```

在构造函数中，成员是在函数体执行前被初始化的，且按照在类中出现的顺序被初始化。析构函数则首先执行函数体，然后销毁成员（析构部分），成员按初始化顺序的逆序销毁。析构函数的析构部分是隐式的，成员销毁时发生什么依赖于成员的类型：销毁类类型的成员需要执行成员的析构函数，销毁内置成员什么也不需要做。注意，隐式析构一个内置指针类型的成员不会```delete```它所指向的对象；智能指针是类类型，因此智能指针成员会在析构阶段自动销毁。析构函数体则是作为成员销毁步骤外的另一部分进行的。

无论何时，当对象被销毁时就会自动调用其析构函数，例如：

- 变量离开作用域时被销毁。
- 当一个对象被销毁时，其成员被销毁。
- 标准库容器或数组被销毁时，其元素被销毁。
- 对于动态分配的对象，当对指向它的指针应用```delete```时被销毁。
- 对于临时对象，当创建它的完整表达式结束时被销毁。

因为析构函数自动运行，程序可以分配资源，且通常无需担心资源何时被释放。

```c++
{  // 新作用域。
    Sales_data *p = new Sales_data;
    auto p2 = make_shared<Sales_data>();
    Sales_data item(*p);
    vector<Sales_data> vec;
    vec.push_back(*p2);
    delete p;
}
/* 退出局部作用域。
 * 对item、p2与vec调用析构函数。
 * 销毁p2会递减其引用计数，如果引用计数变为0，则其指向的对象被释放。
 * 销毁vec会销毁它的元素。
 */
```

#### 合成的析构函数

如果一个类没有显式定义析构函数，则编译器会为其生成一个**合成的析构函数（synthesized destructor）**。某些情况下，合成的析构函数可能阻止我们销毁该类型的对象，否则，合成的析构函数的函数体为空。

```c++
// 等价于合成的析构函数。
class Sales_data {
public:
    ~Sales_data() {}
    // ...
}
```

### =default

通过将拷贝控制成员（或默认构造函数）定义为```=default```的来显式要求编译器生成合成版本的拷贝控制成员，```=default;```用于替代函数体。不能对其他成员函数使用```=default```。

当在类内使用```=default```时，合成的函数被隐式声明为```inline```的。如果不希望合成的函数是```inline```的，需要在类外定义时使用```=default```。

```c++
class Sales_data {
    Sales_data() = default;
    Sales_data(const Sales_data&) = default;
    Sales_data &operator=(const Sales_data &);
    ~Sales_data() = default;
    // ...
};
Sales_data &Sales_data::operator=(const Sales_data&) = default;  // 非inline函数。
```

### 阻止拷贝

虽然大多数应该定义（并且通常也这么做了）拷贝构造函数与拷贝赋值运算符，但对于某些类来说，这些操作没有意义。此时需要采用某种机制阻止拷贝或赋值。例如```iostream```类阻止拷贝，以避免多个对象写入或读取相同的IO缓冲。

通过将拷贝控制成员定义为**删除的函数（deleted function）**（使用```=delete;```替代函数体）来阻止拷贝。删除的函数虽然被声明但不能以其他任何方式被使用。

```=delete```必须出现在函数第一次声明时，而```=default```则不需要。因为一个默认的成员只影响编译器生成什么代码，因此```=default```直到编译器生成代码时才需要，而编译器需要知道一个函数是删除的，以便禁止试图使用它的操作。

```=delete```与```=default```的另一个不同之处在于，可以对任何函数指定```=delete```。

> Also unlike ```= default```, we can specify ```= delete``` on any function (we can use ```= default``` only on the default constructor or a copy-control member that the compiler can synthesize). Although the primary use of deleted functions is to suppress the copy-control members, deleted functions are sometimes also useful when we want to guide the function-matching process.

如果析构函数是删除的，就无法销毁此类型的对象了。如果一个类的析构函数是删除的，则编译器不允许定义该类的变量或创建该类型的临时变量，因为这样的变量无法被销毁；如果一个类的某个成员的析构函数是删除的，则无法同样无法定义该类的变量或创建该类型的临时变量，因为该成员无法被销毁，导致该变量也无法被销毁。

如果一个类的析构函数是删除的，可以动态分配此类的对象，但是不能释放此对象。

```c++
struct NoDtor {
    NoDtor() = default;
    ~NoDtor() = delete;
};
NoDtor nd;  // 错误。
NoDtor *p = new NoDtor();  // 正确。
delete p;  // 错误。
```

#### 合成的拷贝控制成员可能是删除的

在以下情况下，编译器会将某些合成的拷贝控制成员定义为删除的：

- 如果类的某个成员的析构函数是删除的或不可访问的（如```private```的），则类的合成的析构函数被定义为删除的，否则可能创建出无法销毁的对象。
- 如果类的某个成员的拷贝构造函数是删除的或不可访问的，则类的合成的拷贝构造函数被定义为删除的。如果类的某个成员的析构函数是删除的或不可访问的，则类的合成的拷贝构造函数也被定义为删除的，否则可能创建出无法销毁的对象。
- 如果类的某个成员的拷贝赋值运算符是删除的或不可访问的，或类有一个```const```的或引用的成员，则类的合成的拷贝赋值运算符被定义为删除的。因为无法给```const```成员赋值；赋值给引用成员改变的是引用指向的对象的值，如果含有引用成员的类合成拷贝赋值运算符，那么赋值后，左侧运算对象仍然指向与赋值前一样的对象，而不会与右侧对象指向相同的对象。
- 如果类的某个成员的析构函数是删除的或不可访问的，或类有一个没有类内初始化器的引用成员，或类有一个```const```成员，它没有类内初始化器且其类型未显式定义一个默认构造函数，则该类的默认构造函数被定义为删除的。

本质上，如果一个类有数据成员不能构造、拷贝、赋值或销毁，则对应的成员函数被定义为删除的。

#### private拷贝控制

在```=delete```被发布前，类通过将其拷贝控制成员声明为```private```的来阻止拷贝。现已不推荐使用这种方法阻止拷贝。

```c++
class PrivateCopy {
    PrivateCopy(const PrivateCopy&);
    PrivateCopy &operator=(const PrivateCopy&);
    // ...
public:
    PrivateCopy() = default;
    ~PrivateCopy();  // 析构函数是public的，可以定义该类的对象。
};
```

由于某个拷贝控制成员被声明为```private```的，因此用户代码不能拷贝这样的对象（否则导致编译时错误）。为了阻止友元与成员函数拷贝，拷贝控制成员被声明为```private```的但并未定义，而试图访问这样的成员将导致链接时错误。

### 三/五法则（the rule of three/five）

**法则（rule of thumb）1**：如果一个类需要析构函数，则几乎肯定它也需要一个拷贝/移动构造函数与拷贝/移动赋值运算符。

```c++
// HasPtr有个析构函数，但没有拷贝/移动构造函数与拷贝/移动赋值运算符。
class HasPtr {
public:
    HasPtr(const string &s = string()): ps(new string(s)), i(0) {}
    ~HasPtr() {
        delete ps;
    }
    // ...
};

HasPtr f(HasPtr hp) {
    HasPtr ret = hp;
    // ...
    return ret;  // ret与hp被销毁。
}

HasPtr p("some values");
f(p);  // 当f结束时，p.ps指向的内存被销毁。
HasPtr q(p);  // 错误：p与q都指向无效内存。
```

**法则2**：如果一个类需要拷贝/移动构造函数，则几乎肯定它也需要一个拷贝/移动赋值运算符，反之亦然；如果一个类需要一个拷贝/移动构造函数或拷贝/移动赋值运算符，则其未必需要析构函数。例如，一个类为每个对象分配一个唯一的序列号。这个类需要拷贝构造函数为每个新创建的对象生成一个新的、独一无二的序列号，并从给定对象拷贝其他所有对象。同理，这个类需要自定义拷贝赋值运算符。但是这个类不需要自定义析构函数。

通常，如果类定义了任何一个拷贝操作，则其应该定义所有5个拷贝操作。由于拷贝资源通常会带来一些开销，当拷贝非必要的时候，定义了移动构造函数与移动赋值运算符的类可以避免这些开销。

### 拷贝控制与资源管理

通常，管理类外资源的类必须定义拷贝控制成员，这种类需要析构函数来释放对象分配的资源，根据三/五法则，这样的类几乎肯定也需要一个拷贝构造函数与一个拷贝赋值运算符。

```c++
// 行为像值的类：拥有自己的状态。当拷贝行为像值的对象时，原对象与副本完全独立。

class HasPtr {
public:
    HasPtr(const string &s = string()): ps(new string(s)), i(0) {}
    HasPtr(const HasPtr &p): ps(new string(*p.ps)), i(p.i) {}  // 拷贝构造函数，完成string的拷贝，而不是指针的拷贝。
    HasPtr &operator=(const HasPtr&);  // 拷贝赋值运算符，释放当前对象的string，并从右侧运算对象拷贝string。
    ~HasPtr() {  // 析构函数，释放string。
        delete ps;
    }
private:
    string *ps;  // 每个HasPtr对象拥有自己的ps指向的string。
    int i;
};

/* 拷贝赋值运算符通常组合了析构函数与拷贝构造函数的操作。
 * 类似析构函数，拷贝赋值运算符会销毁左侧运算对象的资源。
 * 类似拷贝构造函数，拷贝赋值运算符从右侧运算对象拷贝数据
 */
// 注意要保证一个对象赋值给自身，拷贝赋值运算符也能正确工作。
// 还要尽可能保证拷贝赋值运算符是异常安全的——当异常发生时，能将左侧运算对象置于一个有意义的状态。
HasPtr &HasPtr::operator=(const HasPtr &rhs) {
    auto newp = new string(*rhs.ps);  // 先拷贝底层string。
    delete ps;  // 再释放旧内存。从而保证正确处理自赋值，并且是异常安全的。
    ps = newp;
    i = rhs.i;
    return *this;
}

// 一个实现错误的拷贝赋值运算符，无法正确处理自赋值。
HasPtr &HasPtr::operator=(const HasPtr &rhs) {
    delete ps;
    ps = new string(*(rhs.ps));  // 未定义：如果rhs与*this指向同一对象，则将从已释放的内存中拷贝数据。
    i = rhs.i;
    return *this;
}
```

```c++
// 行为像指针的类：共享状态。当拷贝行为像指针的对象时，原对象与副本使用相同的底层数据。
// 采用引用计数（reference count）。
/* 计数器不能直接作为HasPtr的成员。否则：
 * HasPtr p1("Hiya");
 * HasPtr p2(p1);
 * HasPtr p3(p1);  // 如何更新p2的引用计数？
 * 可以将其保存到动态内存中。
 */

class HasPtr {
    HasPtr(const string &s = string()): ps(new string(s)), i(0), use(new size_t(1)) {}
    HasPtr(const HasPtr &p): ps(p.ps), i(p.i), use(p.use) {  // 拷贝构造函数，拷贝指针而不是string。
        ++*use;
    }
    HasPtr &operator=(const HasPtr&);
    ~HasPtr();
private:
    string *ps;
    int i;
    size_t *use;  // 引用计数。
};

// 析构函数。
// 如果引用计数为0，则释放ps与use。
HasPtr::~HasPtr() {
    if (--*use == 0) {
        delete ps;
        delete use;
    }
}

// 拷贝赋值运算符。
// 同样需要处理自赋值。
HasPtr &HasPtr::operator=(const HasPtr &rhs) {
    ++*rhs.use;  // 先递增rhs的引用计数。  
    if (--*use == 0) {  // 再递减本对象的引用计数。
        delete ps;
        delete use;
    }
    ps = rhs.ps;
    i = rhs.i;
    use = rhs.use;
    return *this;
}
```

### 交换操作

除了定义拷贝控制成员，管理资源的类经常还会定义一个名为```swap```的函数。对于那些与重排元素的算法一起使用的类，定义```swap```是非常重要的。这类算法在需要交换两个元素时就会调用```swap```。

一个类如果定义了自己的```swap```，则算法将使用类自定义的版本，否则算法将使用标准库定义的```swap```。概念上，交换两个对象需要进行一次拷贝与两次赋值。自定义的```swap```有时更有效（例如：交换数据结构而不是内容）。自定义```swap```的类经常通过```swap```来定义它的赋值运算符，这被称为**拷贝并交换（copy and swap）**，这种技术将左侧运算对象与右侧运算对象的一个副本进行交换。

> ```swap``` Functions Should Call ```swap```, Not ```std::swap```

```c++
// HasPtr：行为像值的HasPtr。
/* 原则上，这些内存分配都是不必要的。
 * 交换指针会更有效。
 * string *temp = v1.ps;
 * v1.ps = v2.ps;
 * v2.ps = temp;
 */
HasPtr temp = v1;
v1 = v2;
v2 = temp;

// 自定义HasPtr的swap成员。
class HasPtr {
    friend void swap(HasPtr&, HasPtr&);
    // ...
};
inline void swap(HasPtr &lhs, HasPtr &rhs) {
    /* 如果类的成员自定义了swap，则不要调用std::swap。
     * 假设Foo有成员HasPtr p，则下面的代码会调用std::swap，导致对HasPtr string成员的不必要的拷贝。
     * void swap(Foo &lhs, Foo &rhs) {
     *     std::swap(lhs.h, rhs.h);
     * }
     * 正确写法是：
     * void swap(Foo &lhs, Foo &rhs) {
     *     using std::swap;
     *     swap(lhs.h, rhs.h);  // 调用自定义版本的swap，因为std::swap为模板，优先级低。
     * }
     */
    using std::swap;  // ps与i为内置类型，因为无内置类型版本的swap，所以使用std::swap；但是如果成员自定义了swap，这行语句就很重要了。
    swap(lhs.ps, rhs.ps);
    swap(lhs.i, rhs.i);
}

// 定义类swap的类经常通过swap来定义它的赋值运算符。
// 该技术自动处理自赋值（改变左侧运算对象前先拷贝右侧运算对象），且天然是异常安全的。这和原来定义的赋值运算符的逻辑一致。
HasPtr &HasPtr::operator=(HasPtr rhs) {  // 形参不是引用，rhs是右侧运算对象的一个副本。
    swap(*this, rhs);
    return *this;  // *this是右侧运算对象的副本。
}  // 函数结束，rhs运行析构函数，释放rhs现在指向的内存，即左侧运算对象原来指向的内存。
```

```c++
// 拷贝控制示例。
/* Message类与Folder类。
 * Message表示电子邮件（或其他类型的）消息，Folder表示消息目录。
 * 每个Message可以出现在多个Folder中，但是任意给定的Message只有一个副本。如果Message被改变，则从它所在的任何Folder来浏览此Message，都会看到改变后的内容。
 * 这里忽略向Message添加或删除给定的Folder*的操作（类似Folder的addMsg与remMsg操作）。
 * 这里忽略Folder类的实现，包括它的addMsg与remMsg成员函数。
 */
class Message {
	friend class Folder;
	friend void swap(Message &lhs, Message &rhs);
public:
	explicit Message(const string &str = ""): contents(str) {}
	Message(const Message&);
	Message &operator=(const Message&);
	~Message();
	void save(Folder&);
	void remove(Folder&);
private:
	string contents;
	set<Folder*> folders;
	void add_to_Folders(const Message&);
	void remove_from_Folders();
};
void Message::save(Folder &f) {
	folders.insert(&f);
	f.addMsg(this);
}
void Message::remove(Folder &f) {
	folders.erase(&f);
	f.remMsg(this);
}
void Message::add_to_Folders(const Message &m) {
	for (auto f : m.folders) {
		f->addMsg(this);
	}
}
Message::Message(const Message &m): contents(m.contents), folders(m.folders) {
	add_to_Folders(m);
}
void Message::remove_from_Folders() {
	for (auto f : folders) {
		f->remMsg(this);
	}
}
Message::~Message() {
	remove_from_Folders();
}
Message &Message::operator=(const Message &rhs) {
	remove_from_Folders();
	contents = rhs.contents;
	folders = rhs.folders;
	add_to_Folders(rhs);
	return *this;
}
void swap(Message &lhs, Message &rhs) {
	using std::swap;
	for (auto f : lhs.folders) {
		f->remMsg(&lhs);
		}
	for (auto f : rhs.folders) {
		f->remMsg(&rhs);
	}
	swap(lhs.folders, rhs.folders);
	swap(lhs.contents, rhs.contents);
	for (auto f : lhs.folders) {
		f->addMsg(&lhs);
	}
	for (auto f : rhs.folders) {
		f->addMsg(&rhs);
	}
}
```

```c++
// 一个动态管理内存类。
/*
 * elements：指向分配的内存的首元素。
 * first_free：指向最后一个实际元素的后一位置。
 * cap：指向分配的内存末尾的后一位置。
 */

class StrVec {
public:
    StrVec(): elements(nullptr), first_free(nullptr), cap(nullptr) {}
    StrVec(const StrVec&);
    StrVec &operator=(const StrVec&);
    ~StrVec();
    void push_back(const string&);
    size_t size() const {
        return first_free - elements;
    }
    size_t capacity() const {
        return cap - elements;
    }
    string *begin() const {
        return elements;
    }
    string *end() const {
        return first_free;
    }
    // ...
private:
    allocator<string> alloc;
    void chk_n_alloc() {
        if (size() == capacity()) {
            reallocate();
        }
    }
    pair<string*, string*> alloc_n_copy(const string*, const string*);
    void free();
    void reallocate();
    string *elements;
    string *first_free;
    string *cap;
};

void StrVec::push_back(const string &s) {
    chk_n_alloc();
    alloc.construct(first_free++, s);
}

pair<string*, string*> StrVec::alloc_n_copy(const string *b, const string *e){
    auto data = alloc.allocate(e - b);
    return {data, uninitialized_copy(b, e, data)};
}

void StrVec::free() {
    if (elements) {
        for (auto p = first_free; p != elements;) {  // 逆序销毁元素。
            alloc.destroy(--p);
        }
        alloc.deallocate(elements, cap - elements);
    }
}

StrVec::StrVec(const StrVec &s) {
    auto newdata = alloc_n_copy(s.begin(), s.end());
    elements = newdata.first;
    first_free = cap = newdata.second;
}

StrVec::~StrVec() {
    free();
}

StrVec &StrVec::operator=(const StrVec &rhs) {
    auto data = alloc_n_copy(rhs.begin(), rhs.end());
    free();
    elements = data.first;
    first_free = cap = data.second;
    return *this;
}

void StrVec::reallocate() {
    auto newcapacity = size() ? 2 * size() : 1;
    auto newdata = alloc.allocate(newcapacity);
    auto dest = newdata;
    auto elem = elements;
    for (size_t i = 0; i != size(); ++i) {
        alloc.construct(dest++, std::move(*elem++));  // 使用std::move请求使用string的移动构造函数，避免在重新分配内存时对string的拷贝（可以想象每个string有一个指向char数组的指针，假定string的移动构造函数拷贝了指针而非分配内存并拷贝所有字符）。
    }
    free();  // 释放原来使用的内存。
    elements = newdata;
    first_free = dest;
    cap = elements + newcapacity;
}
```

### 对象移动

从一个对象移动数据并不会销毁该对象，但是有时移动操作完成后，源对象会被销毁。因此，当编写移动操作时，必须保证移后源对象处于可析构的状态（可以安全地被销毁）。除此以外，移动操作还要保证源对象仍然是有效（valid）的，即可以为其赋新值或在不依赖于其当前值的情况下被按照其他方式使用，因为移动操作对移后源对象中的值没有任何要求，因此程序不应该依赖于移后源对象中的值。例如，当从标准库容器中移动数据时，移后源对象仍然有效，可以对其执行```empty```或```size```操作，但是我们不知道其结果是什么（不保证为空）。

> Moving from an object does not destroy that object: Sometime after the move operation completes, the moved-from object will be destroyed. Therefore, when we write a move operation, we must ensure that the moved-from object is in a state in which the destructor can be run.

> In addition to leaving the moved-from object in a state that is safe to destroy, move operations must guarantee that the object remains valid. In general, a valid object is one that can safely be given a new value or used in other ways that do not depend on its current value. On the other hand, move operations have no requirements as to the value that remains in the moved-from object. As a result, our programs should never depend on the value of a moved-from object.

在类实现代码（如移动构造函数或移动赋值运算符）外，只有当确信需要移动操作，并且移动操作保证是安全的（确保源对象没有其他用户）才能使用```std::move```。

#### 移动构造函数

如果一个构造函数的第一个形参是自身类类型的右值引用，并且任何其他的形参都有默认值，则该构造函数是移动构造函数。

移动构造函数用于移动（而不是拷贝）资源，移动构造函数必须保证：销毁移动源是无害的。特别地，一旦资源完成移动，源对象不能再指向被移动的资源——这些资源的所有权已归属新创建的对象。

> In addition to moving resources, the move constructor must ensure that the moved-from object is left in a state such that destroying that object will be harmless. In particular, once its resources are moved, the original object must no longer point to those moved resources—responsibility for those resources has been assumed by the newly created object.

由于移动操作“窃取”资源，它通常不分配任何资源，因此移动操作通常不会抛出异常，应该将其声明为```noexcept```的，否则编译器会认为移动操作可能抛出异常从而做额外的工作。

```c++
// 为StrVec定义移动构造函数。
class StrVec {
public:
    StrVec(StrVec&&) noexcept;
    // ...
};

StrVec::StrVec(StrVec &&s) noexcept: elements(s.elements), first_free(s.first_free), cap(s.cap) {
    s.elements = s.first_free = s.cap = nullptr;  // 令s进入这样的状态：对其运行析构函数是安全的。如果忘记改变s，则销毁移动源对象将释放刚刚移动的内存。
}
```

#### 移动赋值运算符

移动赋值运算符接受一个自身类类型的右值引用。类似移动赋值运算符，移动赋值运算符通常返回左侧运算对象的（左值）引用，并需要正确处理自赋值。类似移动构造函数，移动副赋值运算符通常被声明为```noexcept```的。

```c++
// 为StrVec定义移动赋值运算符。
StrVec &StrVec::operator=(StrVec &&rhs) noexcept {
    if (this != &rhs) {  // 检测自赋值。
        free();
        elements = rhs.elements;
        first_free = rhs.first_free;
        cap = rhs.cap;
        rhs.elements = rhs.first_free = rhs.cap = nullptr;  // 令rhs处于可析构状态。此时StrVec的状态相当于默认初始化时的状态。
    }
    return *this;
}
```

#### 合成的移动操作

如果类定义了自己的拷贝构造函数、拷贝赋值运算符或析构函数，则编译器不会为其合成移动构造函数与移动赋值运算符。

只有当一个类没有定义任何自己的拷贝控制成员，并且类的所有非静态数据成员都可以被移动，编译器才会为该类合成一个拷贝构造函数或拷贝赋值运算符。

编译器可以移动内置类型的成员，如果一个成员是类类型，且该类有对应的移动操作，则编译器也能移动该成员。

```c++
// 编译器会为X合成移动操作。
struct X {
    int i;  // 内置类型可以移动。
    string s;  // string定义了自己的移动操作。
};
// 编译器会为hasX合成移动操作。
struct hasX {
    X mem;
};
X x, x2 = std::move(x);  // 使用合成的移动构造函数。
hasX, hx, hx2 = std::move(hx);  // 使用合成的移动构造函数。
```

与拷贝操作不同，移动操作永远不会被隐式定义为删除的。但是，如果显式地要求编译器生成```=default```的移动操作，并且编译器不能移动所有成员，则编译器会将移动操作定义为删除的函数。除了一个重要例外，什么时候将合成的移动操作定义为删除的遵循与定义删除的合成拷贝控制操作类似的原则：

- 与拷贝构造函数不同，如果类成员定义了自己的拷贝构造函数且未定义移动构造函数，或有类成员未定义自己的拷贝构造函数，并且编译器无法为其合成移动构造函数，则类的移动构造函数被定义为删除的。
- 如果类成员的移动构造函数或移动赋值运算符被定义为删除的或不可访问的，则类的移动构造函数或移动赋值运算符被定义为删除的。
- 类似拷贝构造函数，如果类的析构函数被定义为删除的或不可访问的，则类的移动构造函数被定义为删除的。
- 类似拷贝赋值运算符，如果类有```const```或引用成员，则类的移动赋值运算符被定义为删除的。

```c++
// hasY有一个删除的移动构造函数。
struct hasY {
    hasY() = default;
    hasY(hasY&&) = default;
    Y mem;  // Y为一个类，它定义了自己的拷贝构造函数，但是没有移动构造函数。
}
hasY hy, hy2 = std::move(hy);  // 错误：移动构造函数是删除的。
```

如果一个类定义了一个移动构造函数或移动赋值运算符，则类的合成的拷贝构造函数与拷贝赋值运算符都被定义为删除的。

#### 匹配原则

如果一个类同时有拷贝操作与对应的移动操作，则编译器使用通常的函数匹配规则来决定使用哪个构造函数。

```c++
StrVec v1, v2;  // StrVec的拷贝操作接受const StrVec&，因此可以用于任何可以转换为StrVec的类型；移动操作接受一个StrVec&&，只能接受非const的右值。
v1 = v2;  // v2为左值，调用拷贝赋值运算符。
StrVec getVec(istream &);
v2 = getVec(cin);  // getVec(cin)返回右值，调用移动赋值运算符。
```

```c++
class Foo {
public:
    Foo() = default;
    Foo(const Foo&);
    // 其他成员（不包括移动构造函数）。
};
Foo x;
Foo y(x);  // 调用拷贝构造函数：因为x为左值。
Foo z(std::move(x));  // 调用拷贝构造函数：因为没有移动构造函数，可以将std::move(x)转换为const Foo&类型。
```

用拷贝操作替代对应的移动操作几乎肯定是安全的。通常，拷贝操作满足对应的移动操作的要求：拷贝给定操作并将源对象置于有效（valid）状态。拷贝构造函数甚至不会改变源对象的值。

#### 交换操作

定义了移动操作的类也可定义对应的```swap```操作。

```c++
class HasPtr {
public:
    HasPtr(HasPtr &&p) noexcept: ps(p.ps), i(p.i) {
        p.ps = 0;  // 保证销毁移后源对象是安全的。
    }
    HasPtr operator=(HasPtr rhs) {  // 形参不是引用，rhs是右侧运算对象的一个副本。依赖于实参的类型，将调用拷贝构造函数或移动构造函数。因此单一的赋值运算符即实现了两种功能。
        swap(*this, rhs);
        return *this;
    }  // rhs离开其作用域，string成员被销毁。
    // ...
};

hp = hp2;  // 调用拷贝构造函数。
hp = std::move(hp2);  // 调用移动构造函数。
```

```c++
// Message类的移动操作

void Message::move_Folders(Message *m) {
    folders = std::move(m->folders);
    for (auto f : folders) {
        f->remMsg(m);
        f->addMsg(this);
    }
    m->folders.clear();
}

Message::Message(Message &&m): contents(std::move(m.contents)) {
    move_Folders(&m);
}

Message &Message::operator=(Message &&rhs) {
    if (this != &rhs) {
        remove_from_Folders();
        contents = std::move(rhs.contents);
        move_Folders(&rhs);
    }
    return *this;
}
```

## 继承

**面向对象程序设计（object-oriented programming, OOP）**的核心思想是数据抽象、继承与**动态绑定（dynamic binding）**。

类通过**继承（hierarchy）**形成层次关系。通常由层次关系的根部有一个**基类（base class）**，其他类直接或间接地继承基类，这些类被称为**派生类（derived class）**。基类定义层次关系中的公有成员，而每个派生类定义各自特有的成员。

一个类既可以是基类，也可以是派生类。一个基类既可以是**直接基类（direct base）**，也可以是**间接基类（indirect base）**。

```c++
class Base { /* ... */ };  // 基类。
class D1 : public Base { /* ... */ };  // Base是D1的直接基类。
class D2 : public D1 { /* ... */ };  // Base是D2的间接基类。
```

一个派生类的对象既包含一个含有派生类自己定义的非静态成员的子对象，也包含派生类继承的每个基类的子对象（一个基类对应一个子对象）。注意该过程是递归的，从层次关系的根节点开始，一直到每个叶节点。

```c++
// 一个表示书籍定价策略的类。
class Quote {
    Quote() = default;
    Quote(const string &book, double sales_price): bookNo(book), price(sales_price) {}
    string isbn() const {
        return bookNo;
    }
    virtual double net_price(size_t n) const {  // 返回给定数量书籍的销售总额。
        return n * price;
    }
    virtual ~Quote() = default;
private:
    string bookNo;  // 书籍的ISBN编号。
protected:
    double price = 0.0;  // 书籍不打折时的价格。
};
```

派生类通过**类派生列表（class derivation list）**指出它是从哪个（或哪些）类继承而来。类定义时，类派生列表出现在类名后，类的主体的左花括号前，由```:```紧跟逗号分隔的（直接）基类列表组成，其中每个基类前可以有访问说明符。派生列表中的基类不能重复。

```c++
// 一个特定的定价策略：用户购买量达到一定数量后打折。
class Bulk_quote : public Quote {
    Bulk_quote() = default;
    Bulk_quote(const string&, double, size_t, double);  // 覆盖基类，实现折扣策略。
private:
    double net_price(size_t) const override;
    size_t min_qty = 0;  // 表示应用折扣的最低购买量。
    double discount = 0.0;  // 表示折扣。
};

double Bulk_quote::net_price(size_t cnt) const {
    if (cnt >= min_qty) {
        return cnt * (1 - discount) * price;  // 派生类可以使用基类的受保护成员。
    } else {
        return cnt * price;
    }
}
```

派生类的声明包括类名，但不包括派生列表。因为声明的目的是让程序知道一个名字的存在以及该名字表示什么类型的实体。

```c++
class Bulk_quote : public Quote;  // 错误。
class Bulk_quote;  // 正确。
```

如果一个类被用作基类，则该类必须已被定义，因为派生类包含并可能使用其基类成员，派生类需要它们是什么。这意味着一个类不能派生它本身。

```c++
class Quote;  // 类Quote被声明但未被定义。
class Bulk_quote : public Quote {
    // ...
}
```

每个类控制自己的成员的初始化过程并定义各自的接口，要与类的对象交互则应该使用该类的接口，即使该对象是派生类对象的基类部分。尽管派生类对象包含基类成员，派生类也不应直接初始化这些成员（即使从语法上来看可以这么做），而需要使用基类的构造函数初始化其基类部分。在该过程中，基类部分先被初始化，然后按照成员声明的顺序依次初始化派生类的成员。

除非特别指出，派生类对象的基类部分被默认初始化。如果要使用基类的构造函数初始化基类部分，则在派生类的构造函数中，需要使用基类类名加上圆括号包围的实参列表（如果有的话），以调用特定的基类构造函数。

```c++
// Bulk_quote的构造函数，调用Quote初始化其基类部分。
Bulk_quote(const string &book, double p,  size_t qty, double disc) : Quote(book, p), min_qty(qty), discount(disc) {}
```

如果基类定义了一个静态成员，则该静态成员在整个继承体系是是唯一的。不管基类派生出多少个派生类，每个静态成员都只有一个实例。

静态成员遵循通用的访问控制规则，由定义该静态成员的基类控制。

```c++
class Base {
public:
    static void statmem();
};
class Derived : public Base {
    void f(const Derived&);
}

void Derived::f(const Derived &derived_obj) {
    Base::statmem();  // 通过基类访问静态成员。
    Derived::statmem();  // 通过派生类访问静态成员。
    derived_obj.statmem();  // 通过派生类指针访问静态成员。
    statmem();  // 通过this对象访问。
}
```

通过在类名后紧跟```final```关键字，可以阻止该类被继承。

```c++
class NoDerived final { /* ... */ };
class Base { /* ... */ };
class Last final : Base { /* ... */ };
class Bad : NoDerived { /* ... */ };  // 错误。
class Bad2 : Last { /* ... */ };  // 错误。
```

### 类类型转换

当使用存在继承关系的类型时，需要区分一个变量或其他表达式的**静态类型（static type）**与**动态类型（dynamic type）**，前者在编译时已知，后者则直到运行时才可知。

在类的继承体系中，基类的指针（包括智能指针）或引用可以转换为派生类的指针或引用，这被称为**派生类到基类的（derived-to-base）**类型转换。因此，基类的指针或引用的静态类型可能与动态类型不一致。

```c++
// 计算并打印销售总额。
double print_total(ostream &os, const Quote &item, size_t n) {
    double ret = item.net_price(n);  // 根据item形参绑定的对象的类型，调用Quote::net_price或Bulk_quote::net_price。
    os << "ISBN: " << item.isbn() << " # sold: " << n << " total due: " << ret << endl;
    return ret;
}

print_total(cout, basic, 20);  // basic的类型为Quote，调用Quote::net_price。
print_total(cout, bulk, 20);  // bulk的类型为Bulk_quote，调用Bulk_quote::net_price。
```

之所以存在这种转换，是因为每个派生类对象都包括一个基类部分，而基类的引用与指针可以绑定到该基类部分。一个基类的对象既可以独立存在，也可以作为派生类对象的一部分而存在。这同时意味着，不存在从基类向派生类的自动类型转换，否则派生类可能访问基类中不存在的成员。

```c++
Quote base;
Bulk_quote *bulkP = &base;  // 错误。
Bulk_quote &bulkRef = base;  // 错误。
```

即使一个基类指针或引用绑定在一个派生类对象上，也不能执行从基类到派生类的转换。因为编译器只能检查指针或引用的静态类型来判断转换是否合法，编译器无法在编译时确定某个特定的转换在运行时是否安全。如果基类中含有虚函数，则可以通过```dynamic_cast```请求这样的类型转换（如果确定基类到派生类的转换是安全的，也可以使用```static_cast```）。

```c++
Bulk_quote bulk;
Quote *itemP = &bulk;
Bulk_quote *bulkP = itemP;
```

注意派生类向基类的转换只适用于指针或引用类型，在派生类与基类类型之间不存在这样的转换。如果直接用派生类初始化基类（而不是基类的引用或指针）或使用派生类为基类赋值，则会调用基类相应的构造函数或赋值运算符。这些成员通常包含一个该类类型的```const```的引用，因此这些成员可以接受派生类对象；由于这些成员不是虚函数，当给基类的构造函数或赋值运算符传递给一个派生类对象时，实际运行的是基类的构造函数或赋值运算符，因此这些成员只能处理基类自己的成员，导致派生类部分被**切掉（sliced down）**。

```c++
Bulk_quote bulk;
Quote item(bulk);  // 调用Quote::Quote(const Quote&)，该函数是合成的。
item = bulk;  // 调用Quote::operator=(const Quote&)，该函数是合成的。
```

### 虚函数

如果类想要派生类自定义适合自身的版本，则基类需要将这些函数定义为**虚（virtual）函数**，通过在成员函数声明前使用```virtual```关键字定义一个虚函数，```virtual```不能出现在类外的成员函数定义处。

任何构造函数之外的非静态函数都可以是虚函数。

如果基类将一个函数定义为虚函数，则在派生类中，该函数可以覆盖（override）从基类继承而来的旧定义，否则派生类直接继承该函数而不作改变。如果派生类没有覆盖基类中的某个虚函数，则该派生类继承其在基类中的版本。

如果基类将一个函数定义为虚函数，则在派生类中，该函数也是虚函数，不管它有没有使用```virtual```关键字限定（注意如何派生类没有覆盖基类中的某个虚函数，也会存在一个继承的虚函数）。

```c++
// Quote的net_price函数，应该定义为虚函数，以便派生类根据给定策略给定数量书籍的销售总额。
virtual double net_price(size_t n) const {
    return n * price;
}
```

成员函数如果未被声明为虚函数，则其解析过程发生在编译时而非运行时。当虚函数通过指针或引用调用时，编译器直到运行时才能确定调用哪个的函数，实际调用的虚函数是与绑定到指针或引用上的对象的动态类型相匹配的那一个。这种特性即动态绑定或**运行时绑定（run-time binding）**，只有当使用基类的指针或引用调用虚函数时会发生自动绑定。

> Calls to Virtual Functions May Be Resolved at Run Time

> When a virtual function is called through a reference or pointer, the compiler generates code to decide at run time which function to call. The function that is called is the one that corresponds to the dynamic type of the object bound to that pointer or reference.

```c++
// Quote的isbn函数，不应该定义为虚函数。
string isbn() const {
    return bookNo;
}
```

```c++
Quote base("0-201-82470-1", 50);
print_total(cout, base, 10);  // 调用Quote::net_price
Bulk_quote derived("0-201-82470-1", 50, 5, .19);
print_total(cout, derived, 10);  // 调用Bulk_quote::net_price
```

```c++
base = derived;  // 将derived的Quote部分拷贝到base。
base.net_price(20);  // 调用Quote::net_price，不会动态绑定。
```

如果派生类的一个函数覆盖了某个继承而来的虚函数，则该函数的形参类型与返回类型必须与被其覆盖的虚函数完全一致。一个例外是：这两个虚函数可以返回两个类型的引用或指针，这两个类型通过继承关联。即：如果类```D```由类```B```派生得到，则基类的虚函数可以返回```B```的引用或指针，而派生类对应的虚函数可以返回```D```的引用或指针，前提是要求从```D```到```B```的转换是可访问的。

如果派生类定义了一个函数，其与基类中某个虚函数的名字相同，但形参列表不同，则编译器认为这两个函数相互独立，派生类中的函数未覆盖基类中的虚函数。在实践中，这常常意味着错误，即我们希望派生类覆盖基类中的虚函数，但不小心把形参列表弄错了。可以在虚函数声明时，在形参列表（包括```const```或引用修饰符）后与尾置返回类型前放置```override```关键字，指定该虚函数必须覆盖基类中的虚函数。如果```override```标记了某个函数，但是该函数没有覆盖已存在的虚函数，则编译器报错。

```c++
struct B {
    virtual void f1(int) const;
    virtual void f2();
    void f3();
};
struct D1 : B {
    void f1(int) const override;  // 正确。
    void f2(int) override;  // 错误，B中没有形如f2(int)的函数。
    void f3() override;  // 错误，f3不是虚函数。
    void f4() override;  // 错误，B中没有名为f4的函数。
}
```

同样地，可以在函数的相同位置放置```final```关键字，指定该函数不可被覆盖。覆盖一个被标记为```final```的函数会引发错误。

```c++
struct D2 : B {
    void f1(int) const final;
};
struct D3 : D2 {
    void f2();  // 正确，覆盖从间接基类B继承而来的虚函数。
    void f1(int) const;  // 错误，不允许被覆盖。
}
```

虚函数也可以由默认实参，其默认实参由调用的静态类型决定。因此，基类与派生类中的虚函数的默认实参最好相同。

在某些情况下，我们希望对虚函数的调用不要进行动态绑定，而是强迫调用某个特定版本的虚函数。可以使用作用域运算符实现该目的。通常，只有成员函数或友元中的代码才需要使用回避虚函数机制。特别地，当派生类的虚函数调用基类版本的虚函数时，基类的虚函数通常完成继承层次中所有类型都要做的共同任务，而派生类中虚函数则做与特定于派生类本身类型的额外工作。

```c++
double undiscounted = baseP -> Quote::net_price(42);  // 如果没有使用作用域运算符，则无限递归。
```

#### 纯虚函数

**纯虚（pure virtual）函数**是一种虚函数，但是其本身没有意义，而是作为其他虚函数的接口。

通过将函数体取代为```=0```定义一个纯虚函数，其中```=0```只能出现在类内部的虚函数声明处。我们可以为纯虚函数提供定义，但是函数体必须定义在类的外部。

含有（或未经覆盖直接继承）纯虚函数的类是**抽象基类（abstract base class）**，抽象基类负责定义接口，其他类可以覆盖该接口。不能（直接）创建抽象基类的对象。

```c++
// 一个表示折扣策略的类。
class Disc_quote : public Quote {
public:
    Disc_quote() = default;
    Disc_quote(const string &book, double price, size_t qty, double disc): Quote(book, price), quantity(qty), discount(disc) {}
    double net_price(size_t) const = 0;
protected:
    size_t quantity = 0;  // 表示书籍购买量。
    double discount = 0.0;  // 表示折扣。
};

// 重新实现Bulk_quote，让其继承自Disc_quote，表示一个具体的折扣策略。
// 这是重构（refactoring）（重新设计类的层次体系，将操作和/或数据从一个类中移动到另一个类中）的一个例子。使用了Bulk_quote或Quote的代码无需改动，但是必须重新编译使用这些类的代码。
class Bulk_quote : public Disc_quote {
public:
    Bulk_quote() = default;
    Bulk_quote(const string &book, double price, size_t qty, double disc): Disc_quote(book, price, qty, disc) {}  // 每个类控制自己的初始化过程并定义各自的接口。
    double net_price(size_t) const override;  // 需要覆盖基类的虚函数，否则Bulk_quote仍然是抽象基类。
};

Disc_quote discounted;  // 错误。
Bulk_quote bulk;  // 正确。
```

### 继承中的类作用域

每个类定义自己的作用域，在该作用域内我们定义它的成员。当存在继承关系时，派生类的作用域嵌套在基类的作用域内。如果一个名字在派生类的作用域内无法被解析，则会在外层基类作用域内寻找该名字的定义。

```c++
Bulk_quote bulk;
cout << bulk.isbn();  // 首先在Bulk_quote中查找isbn，没有找到；然后再Disc_quote中查找，仍然找不到；最后在Quote中查找，找到isbn。
```

一个对象、引用或指针的静态类型决定了该对象的哪些成员是可见的。

```c++
class Disc_quote : public Quote {
public:
    // 作为一个例子，给Disc_quote添加新成员，返回一个保存最小（或最大）数量与折扣价格的pair。
    pair<size_t, double> discount_policy() const {
        return {quantity, discount};
    }
    // ...
}

Bulk_quote bulk;
Bulk_quote *bulkP = &bulk;
Quote *itemP = &bulk;
bulkP->discount_policy();  // 正确。
itemP->discount_policy();  // 错误，item的类型为Quote*。
```

派生类中的名字会隐藏外层基类中的同名名字。

```c++
struct Derived : Base {
    Base(): mem(0) {}
protected:
    int mem;
};
struct Derived : Base {
    Derived(int i): mem(i) {}  // i初始化Derived::mem。
    int get_mem() {
        return mem;  // 返回Derived::mem。
    }
protected:
    int mem;  // 隐藏基类中的mem。
};

Derived d(42);
cout << d.get_mem() << endl;  // 打印42。
```

```c++
struct Derived : Base {
    int get_base_mem() {
        return Base::mem;  // 通过作用域运算符来访问被隐藏的基类成员。
    }
}
```

假设调用了```p -> mem()```或```obj.mem()```，则以下4个步骤被依次执行：

1. 确定```p```或```obj```的静态类型，这里必然是类类型。
2. 在```p```或```obj```的静态类型对应的类中查找```mem```，如果没有找到，则在直接基类中查找，一直下去直到找到```mem```，或在整个类链中都没有找到而无法编译。
3. 如果```mem```被找到，则执行正常的类型检查以确认当前找到的定义是否合法。
4. 如果调用合法：如果```mem```为虚函数且通过引用或指针调用之，则执行动态绑定；否则产生普通的函数调用。

一如往常，名字查找先于类型检查。

```c++
struct Base {
    int memfcn();
};
struct Derived : Base {
    int memfcn(iint);
};
Derived d; Base b;
b.memfcn();  // 调用Base::memfcn。
d.memfcn(10);  // 调用Derived::memfcn。
d.memfcn();  // 错误，实参为空的memfcn被隐藏。
d.Base::memfcn();  // 正确。
```

> We can now understand why virtual functions must have the same parameter list in the base and derived classes (§15.3, p. 605). If the base and derived members took arguments that differed from one another, there would be no way to call the derived version through a reference or pointer to the base class. 

```c++
class Base {
public:
    virtual int fcn();
};
class D1 : public Base {
public:
    int fcn(int);  // 隐藏Base::fcn，这个fcn不是虚函数。D1继承了Base::fcn()的定义。
    virtual void f2();
};
class D2 : public D1 {
public:
    int fcn(int);  // 非虚函数，隐藏D1::fcn(int)。
    int fcn();  // 覆盖Base::fcn。
    void f2();  // 覆盖D1::f2.
};

Base bobj; D1 d1obj; D2 d2obj;
Base *bp1 = &bobj, *bp2 = &d1obj, *bp3 = &d2obj;
bp1->fcn();  // 虚调用Base::fcn。
bp2->fcn();  // 虚调用Base::fcn。
bp3->fcn();  // 虚调用D2::fcn。
D1 *d1p = &d1obj; D2 *d2p = &d2obj;
bp2->f2();  // 错误，Base没有名为f2的成员。
d1p->f2();  // 虚调用D1::f2()。
d2p->f2();  // 虚调用D2::f2()。

Base *p1 = &d2obj; D1 *p2 = &d2obj; D2 *p3 = &d2obj;
p1->fcn(42);  // 错误，Base没有接受int的fcn。
p2->fcn(42);  // 静态绑定，调用D1::fcn(int)。
p3->fcn(42);  // 静态绑定，调用D2::fcn(int)。
```

如果派生类希望基类的成员函数重载版本都可见，则其必须覆盖所有的版本，或一个也不覆盖。

如果要想覆盖重载集合中的一些而非全部函数，则可以使用```using```声明，并定义特有的成员函数版本。此时，```using```声明的正常规则适用：基类成员函数的每个重载实例在派生类中都必须是可访问的。对派生类中没有重新定义的重载版本的访问实际上是对```using```声明点的访问。

> The access to the overloaded versions that are not otherwise redefined by the derived class will be the access in effect at the point of the using declaration.

### 继承中的拷贝控制

#### 虚析构函数

基类通常应该定义一个虚析构函数以便使得我们能动态分配集成体系中的对象。

```c++
class Quote {
public:
    virtual ~Quote() = default;  // 如果删除的是一个指向派生类对象的基类指针，则需要析构函数。
};
```

和其他虚函数一样，析构函数的虚属性也被继承。如果基类定义了虚函数，则派生类无法自定义了析构函数还是使用合成的析构函数，该析构函数都是虚析构函数。

```c++
Quote *itemP = new Quote;
delete itemP;  // 调用Quote的析构函数。
itemP = new Bulk_quote;
delete itemP;  // 调用Bulk_quote的析构函数。
```

如果基类的析构函数不是虚函数，则```delete```一个指向派生类对象的基类指针的行为未定义。

三/五法则的一个重要例外是：基类几乎总是需要析构函数以便使其成为虚函数，因此基类有一个空的虚析构函数并不意味着该类需要拷贝构造函数或赋值运算符。

> The fact that a base class needs a virtual destructor has an important indirect impact on the definition of base and derived classes: If a class defines a destructor—even if it uses ```= default``` to use the synthesized version—the compiler will not synthesize a move operation for that class (§13.6.2, p. 537).

#### 合成的拷贝控制

基类或派生类合成的拷贝控制成员对类本身的成员依次执行初始化、赋值或销毁操作外，还负责使用直接基类中的对应操作对一个对象的直接基类部分执行初始化、赋值或销毁操作。

例如：合成的```Bulk_quote```的默认构造函数运行```Disc_quote```的默认构造函数，后者又运行```Quote```的默认构造函数。```Quote```的默认构造函数默认初始化```bookNo```成员，并使用类内初始值初始化```price```成员。当```Quote```的构造函数结束，继续执行```Disc_quote```的构造函数，其使用类内初始值初始化```qty```与```discount```。当```Disc_quote```的构造函数结束，继续执行```Bulk_quote```的构造函数，它什么也不做。

> The synthesized ```Bulk_quote``` default constructor runs the ```Disc_Quote``` default constructor, which in turn runs the ```Quote``` default constructor.

> It is worth noting that it doesn’t matter whether the base-class member is itself synthesized (as is the case in our ```Quote``` hierarchy) or has a an user-provided definition. All that matters is that the corresponding member is accessible (§ 15.5, p. 611) and that it is not a deleted function.

派生类的合成的析构函数（使用```= default```且被定义为虚函数）的隐式的析构部分除了负责销毁类的成员，还负责销毁直接基类，该直接基类又负责销毁它的直接基类，以此类推直到根基类。

基类、派生类与普通类类似，也会处于同样的原因将合成的默认构造函数或任何拷贝控制成员定义为删除的。除此以外：

- 如果基类的默认构造函数、拷贝构造函数或拷贝赋值运算符为删除的或不可访问的，则派生类中对应的成员被定义为删除的，因为编译器不能使用基类成员来执行派生类对象基类部分的构造、赋值或销毁操作。
- 如果基类的析构函数为删除的或不可访问的，则派生类的合成的默认构造函数与拷贝构造函数被定义为删除的，因此派生类对象的基类部分无法被销毁。
- 与以往一样，编译器不会合成删除的移动操作。如果使用```= default```去请求一个移动操作时，且基类中对应的操作为删除的或不可访问的，则派生类中该操作为删除的函数，因为基类部分不能被移动。如果基类的析构函数为删除的或不可访问的，则派生类的移动构造函数也是删除的。

```c++
class B {
public:
    B();
    B(const B&) = delete;
    // 其他成员，没有移动构造函数。
};
class D : public B {
    // 没有构造函数。
};
D d;  // 正确。D的合成的默认构造函数使用B的默认构造函数。
D d2(d);  // 错误，D的合成的拷贝构造函数是删除的。
D d3(std::move(d));  // 错误，隐式地使用D的删除的拷贝构造函数（D没有移动构造函数）。
```

在实际编程中，如果基类没有默认、拷贝或移动构造函数，则派生类通常也没有这些函数。

大多数基类会定义一个虚析构函数，因此默认情况下，基类通常没有合成的移动操作；如果基类没有合成的移动操作，则默认情况下，派生类中也没有合成的移动操作。因此，如果需要，基类通常应该定义移动操作。

```c++
// Quote必须显式定义移动操作；一旦Quote定义了移动操作，那么它必须同时显式定义拷贝操作。
class Quote {
public:
    Quote() = default;
    Quote(const Quote&) = default;
    Quote(Quote&&) = default;
    Quote &operator=(const Quote&) = default;
    Quote &operator=(Quote&&) = default;
    virtual ~Quote() = default;
    // ...
};
```

#### 派生类的拷贝控制成员

派生类的构造函数、赋值运算符需要同时负责派生类自己成员与派生类对象基类部分的相应操作。但是析构函数只负责销毁派生类自己分配的资源（对象本身被隐式销毁）。对象销毁的顺序与其创建的顺序相反：首先执行派生类的析构函数，然后是基类的析构函数，一直到继承体系的根。

派生类的拷贝/移动构造函数通常（显式地）使用基类的构造函数初始化对象的基类部分，通常不会直接操作基类成员，也不会省略基类成员的初始值（否则基类部分可能被默认初始化，而派生类部分得到拷贝值，导致派生类对象拥有奇怪的配置）。对于派生类的赋值运算符，同理。

```c++
class Base { /* ... */ } ;
class D: public Base {
public:
    D(const D &d): Base(d) { /* ... */ }
    D(D &&d): Base(std::move(d)) { /* ... */ }
};
```

```c++
D &D::operator=(const D &rhs) {
    Base::operator=(rhs);
    // 为派生类成员赋值。需要处理自赋值以及释放已有资源等情况。
    return *this;
}
```

```c++
class D: public Base {
public:
    // Base::~Base被自动调用。
    ~D() { /* ... */ }
};
```

##### 在构造函数与析构函数中调用虚函数

> As we’ve seen, the base-class part of a derived object is constructed first. While the base-class constructor is executing, the derived part of the object is uninitialized. Similarly, derived objects are destroyed in reverse order, so that when a base class destructor runs, the derived part has already been destroyed. As a result, while these base-class members are executing, the object is incomplete.
>
> To accommodate this incompleteness, the compiler treats the object as if its type changes during construction or destruction. That is, while an object is being constructed it is treated as if it has the same class as the constructor; calls to virtual functions will be bound as if the object has the same type as the constructor itself. Similarly, for destructors. This binding applies to virtuals called directly or that are called indirectly from a function that the constructor (or destructor) calls.
>
> To understand this behavior, consider what would happen if the derived-class version of a virtual was called from a base-class constructor. This virtual probably accesses members of the derived object. After all, if the virtual didn’t need to use members of the derived object, the derived class probably could use the version in its base class. However, those members are uninitialized while a base constructor is running. If such access were allowed, the program would probably crash.

> If a constructor or destructor calls a virtual, the version that is run is the one corresponding to the type of the constructor or destructor itself.

#### 继承的构造函数

派生类可以使用标注了（直接）基类名的```using```声明语句“继承”基类（只能是直接基类）的构造函数。

对于基类的每个构造函数，编译器都生成与之对应的派生类构造函数（形参列表相同），生成的构造函数形如：```derived(parms): base(args) {}```，其中```base```为基类名，```derived```为派生类名，```parms```为构造函数形参列表，```args```将派生类构造函数的形参传递给基类构造函数。派生类如果有自己的数据成员，则这些成员被默认初始化。

> where *derived* is the name of the derived class, *base* is the name of the base class, *parms* is the parameter list of the constructor, and *args* pass the parameters from the derived constructor to the base constructor.

```c++
class Bulk_quote : public Disc_quote {
public:
    /* 这个构造函数等价于：
     * Bulk_quote(const string &book, double price, size_t qty, double disc): Disc_quote(book, price, qty, disc) {}
     */
    using Disc_quote::Disc_quote;
    double net_price(size_t) const;
};
```

和普通的```using```声明不同，构造函数的```using```声明不会改变该继承的构造函数的访问级别（继承的```private```构造函数在派生类中仍然是```private```的，等等）。一个```using```声明也不能指定```explicit```或```constexpr```，如果基类的构造函数是```explicit```的或```constexpr```的，则继承的构造函数也 拥有相同的属性。

如果基类构造函数有默认实参，则这些实参不会被继承。并且，此时派生类将会获得多个继承的构造函数，其中每个构造函数分别省略掉一个含有默认实参的形参（包括一个没有省略任何形参的构造函数）。

如果基类有若干构造函数，则除了两种情况，派生类将继承所有这些构造函数。这两种情况是：

- 派生类可以继承一部分构造函数同时为其他构造函数定义自己的版本。如果派生类定义的构造函数与基类的构造函数有相同的形参列表，则该构造函数不会被继承，定义在派生类中的构造函数用于替换继承的构造函数。
- 默认、拷贝、移动构造函数不会被继承，这些构造函数按照正常规则被合成。继承的构造函数不会被当成用户自定义的构造函数。因此，如果一个类只包含继承的构造函数，则其也会有一个合成的默认构造函数。

### 容器与继承

当使用容器存放继承体系中的对象时，必须采取间接存储方式。因为容器中不能保存不同类型的元素，因此不能将具有继承关系的多种类型的对象直接存储到容器中。

```c++
vector<Quote> basket;
basket.push_back(Quote("0-201-82470-1", 50));
basket.push_back(Bulk_quote("0-201-54848-8", 50, 10, .25));  // 正确，但是只能将对象的Quote部分拷贝到basket中，将派生类对象赋值给基类对象导致派生类部分被切掉。
cout << basket.back().net_price(15) << endl;  // 打印750，即15 * 50。
```

当使用容器存放继承体系中的对象时，通常存放的是基类的指针（最好是智能指针）。

```c++
vector<shared_ptr<Quote>> basket;
basket.push_back(make_shared<Quote>("0-201-82470-1", 50));
basket.push_back(make_shared<Bulk_quote>("0-201-54848-8", 50, 10, .25));  // 正确，将派生类的智能指针转换为基类的智能指针，即shared_ptr<Bulk_quote> -> shared_ptr<Quote>。
cout << basket.back() -> net_price(15) << endl;  // 打印562.5，即15 * (1 - 0.25) * 50。
```

```c++
// 一个保存用户想要购买的书籍的类。
// 因为必须使用指针或引用来进行面向对象编程，而指针会增加程序复杂性，因此定义该辅助类。

class Basket {
    // 使用合成的默认构造函数与拷贝控制成员。
    void add_item(const shared_ptr<Quote> &sale) {
        items.insert(sale);
    }
    double total_receipt(ostream&) const;
private:
    static bool compare(const shared_ptr<Quote> &lhs, const shared_ptr<Quote> &rhs) {
        return lhs->isbn() < rhs->isbn();
    }
    multiset<shared_ptr<Quote>, decltype(compare)*> items{compare};  // 可以保存同一本书的多条交易记录。
};

double Basket::total_receipt(ostream &os) const {
    double sum = 0.0;
    for (auto iter = items.cbegin(); iter != items.cend(); iter = items.upper_bound(*iter)) {
        sum += print_total(os, **iter, items.count(*iter));
    }
    os << "Total Sale: " << sum << endl;
    return sum;
}

// 用户需要处理动态内存，需要显式传递一个shared_ptr对象给add_item函数。
Basket bsk;
bsk.add_item(make_shared<Quote>("123", 45));
bsk.add_item(make_shared<Bulk_quote>("345", 45, 3, .15));
```

```c++
// 隐藏指针。
/* 为了隐藏指针，需要定义：
 * void add_item(const Quote &sale);
 * void add_item(Quote &&sale);
 * 但是add_item不知道要分配的类型。由于add_item进行内存分配时需要拷贝或移动它的sale参数，因此可能有表达式：new Quote(sale)。
 * 该表达式会切掉Bulk_quote对象的派生类部分。
 * 因此可以通过模拟虚拷贝（virtual copy）隐藏指针。
 */

class Quote {
public:
    virtual Quote *clone() const & {
        return new Quote(*this);
    }
    virtual Quote *clone() && {
        return new Quote(std::move(*this));
    }
    // ...
};
class Bulk_quote : public Quote {
    Bulk_quote *clone() const & {
        return new Bulk_quote(*this);
    }
    Bulk_quote *clone() && {
        return new Bulk_quote(std::move(*this));
    }
    // ...
};

class Basket {
public:
    void add_item(const Quote &sale) {
        items.insert(shared_ptr<Quote>(sale.clone()));
    }
    void add_item(Quote &&sale) {
        items.insert(shared_ptr<Quote>(std::move(sale).clone()));
    }
    // ...
};
```

```c++
// 扩展的文本查询程序。
/* 支持以下查询：
 *     单词查询：查询匹配某个string的所有行。
 *     逻辑非查询：查询不能匹配某个string的所有行。
 *     逻辑或查询：查询可以匹配两个string中任意一个的所有行。
 *     逻辑与查询：查询同时匹配两个string的所有行。
 * 并能够混合使用以上运算。
 */

// 抽象基类，表示查询。
class Query_base {
	friend class Query;
protected:
	using line_no = TextQuery::line_no;
    virtual ~Query_base() = default;
private:
	virtual QueryResult eval(const TextQuery&) const = 0;
    virtual string rep() const = 0;
};

// 管理Query_base继承体系的接口类。
class Query {
	friend Query operator~(const Query&);
	friend Query operator|(const Query&, const Query&);
	friend Query operator&(const Query&, const Query&);
public:
	Query(const string&);
	QueryResult eval(const TextQuery &t) const {
		return q -> eval(t);
	}
	string rep() const {
		return q -> rep();
	}
private:
	Query(shared_ptr<Query_base> query) : q(query) {}
	shared_ptr<Query_base> q;
};

// Query的输出运算符，打印查询结果。
ostream &operator<<(ostream &os, const Query &query) {
    return os << query.rep();
}

// 单词查询。
class WordQuery : public Query_base {
	friend class Query;
	WordQuery(const string &s) : query_word(s) {}
	QueryResult eval(const TextQuery &t) const {
        return t.query(query_word);
    }
    string rep() const {
        return query_word;
    }
	string query_word;
};

inline Query::Query(const string &s): q(new WordQuery(s)) {}

// 逻辑非查询。
class NotQuery: public Query_base { 
    friend Query operator~(const Query&);
    NotQuery(const Query &q): query(q) {}
    string rep() const {
        return "~(" + query.rep() + ")";
    }
    QueryResult eval(const TextQuery&) const;
    Query query;
};

inline Query operator~(const Query &operand) {
    return shared_ptr<Query_base>(new NotQuery(operand));
}

class BinaryQuery: public Query_base {
protected:
    BinaryQuery(const Query &l, const Query &r, string s): lhs(l), rhs(r), opSym(s) {}
    string rep() const {
        return "(" + lhs.rep() + " " + opSym + " " + rhs.rep() + ")";
    }
    Query lhs, rhs;
    string opSym;
};

// 逻辑与查询。
class AndQuery: public BinaryQuery {
    friend Query operator&(const Query&, const Query&);
    AndQuery(const Query &left, const Query &right): BinaryQuery(left, right, "&") {}
    QueryResult eval(const TextQuery&) const;
};

inline Query operator&(const Query &lhs, const Query &rhs) {
    return shared_ptr<Query_base>(new AndQuery(lhs, rhs));
}

// 逻辑或查询。
class OrQuery: public BinaryQuery {
    friend Query operator|(const Query&, const Query&);
    OrQuery(const Query &left, const Query &right): BinaryQuery(left, right, "|") {}
    QueryResult eval(const TextQuery&) const;
};

inline Query operator|(const Query &lhs, const Query &rhs) {
    return shared_ptr<Query_base>(new OrQuery(lhs, rhs));
}

QueryResult OrQuery::eval(const TextQuery &text) const {
    auto right = rhs.eval(text), left = lhs.eval(text);
    auto ret_lines = make_shared<set<line_no>>(left.begin(), left.end());
    ret_lines->insert(right.begin(), right.end());
    return QueryResult(rep(), ret_lines, left.get_file());
}

QueryResult AndQuery::eval(const TextQuery &text) const {
    auto left = lhs.eval(text), right = rhs.eval(text);
    auto ret_lines = make_shared<set<line_no>>();
    set_intersection(left.begin(), left.end(), right.begin(), right.end(), inserter(*ret_lines, ret_lines -> begin()));
    return QueryResult(rep(), ret_lines, left.get_file());
}

QueryResult NotQuery::eval(const TextQuery &text) const {
    auto result = query.eval(text);
    auto ret_lines = make_shared<set<line_no>>();
    auto beg = result.begin(), end = result.end();
    auto sz = result.get_file()->size();
    for (size_t n = 0; n != sz; ++n) {
        if (beg == end || *beg != n) {
            ret_lines->insert(n);
        } else if (beg != end) {
            ++beg;
        }
    }
    return QueryResult(rep(), ret_lines, result.get_file());
}

// 其中，QueryResult额外定义了以下的公有成员。
class QueryResult {
public:
    set<TextQuery::line_no>::iterator begin() const {
		return lines->begin();
	}
	set<TextQuery::line_no>::iterator end() const {
		return lines->end();
	}
    shared_ptr<vector<string>> get_file() const {
		return file;
	}
    // ...
}
```

### 多重继承与虚继承

#### 多重继承

**多重继承（multiple inheritance）**是指从多个直接基类中产生派生类的能力。在多重继承中，派生类的对象中含有每个基类的子对象。

```c++
class ZooAnimal { /* ... */ };
class Bear : public ZooAnimal { /* ... */ };  // Bear除了包含自己的成员外，还包含ZooAnimal子部分。
class Panda : public Bear, public Endangered { /* ... */ };  // Pandas除了包含自己的成员外，还包含Bear与Endangered子部分。
```

构造一个派生类对象需要同时构造并初始化它的所有基类子对象。与从一个基类派生一样，多重继承的派生类的构造函数的初始值也只能初始化它的直接基类。

```c++
Panda::Panda(string name, bool onExhibit): Bear(name, onExhibit, "Panda"), Endangered(Endangered::critical) {}
Panda::Panda(): Endangered(Endangered::critical) {}
```

基类的构造顺序取决于它们在类派生列表中出现的顺序，与在构造函数中出现的顺序无关。例如，对于一个```Panda```对象，依次初始化其```ZooAnimal```、```Bear```、```Endangered```与```Bear```部分。

派生类可以从一个或多个基类继承构造函数。如果派生类从多于一个基类中继承了同样（即形参列表相同）的构造函数，则报错；此时，派生类必须为该构造函数定义它自己的版本。

```c++
struct Base1 {
    Base1() = default;
    Base1(const string&);
    Base1(shared_ptr<int>);
};
struct Base2 {
    Base2() = default;
    Base2(const string&);
    Base2(int);
};
// 错误，D1试图从两个基类中同时继承D1::D1(const string&)。
struct D1 : public Base1, public Base2 {
    using Base1::Base1;
    using Base2::Base2;
};

// 正确。
struct D2: public Base1, public Base2 {
    using Base1::Base1;
    using Base2::Base2;
    D2(const string &s): Base1(s), Base2(s) {}
    D2() = default;  // 必须显式定义默认构造函数。
}
```

在多重继承中，析构函数的调用顺序同样与构造函数的调用顺序相反，且负责清除类本身分配的资源（派生类的成员与基类都是自动被销毁的）。

在多重继承中，如果派生类定义了自己的拷贝/移动构造函数与赋值运算符，则其必须为整个对象执行拷贝、移动或赋值操作。只有当派生类使用合成的这些成员时，才会自动对其基类部分执行这些操作。在合成的拷贝控制成员中，每个基类通过对应的基类成员被隐式构造、赋值或销毁。拷贝控制成员对类的操作顺序与构造函数对类的操作顺序一致。

多重继承中，类类型转换同样成立。

```c++
void print(const Bear&);
void highlight(const Endangered&);
ostream &operator<<(ostream&, const ZooAnimal&);
Panda ying_yang("ying_yang");
print(ying_yang);
highlight(ying_yang);
cout << ying_yang << endl;
```

编译器不会在派生类向基类的转换中进行区分，转换到任意一种基类都一样好。

> The compiler makes no attempt to distinguish between base classes in terms of a derived-class conversion. Converting to each base class is equally good.

```c++
void print(const Bear&);
void print(const Endangered&);

Panda ying_yang("ying_yang");
print(ying_yang);  // 二义性错误。
```

与从一个基类继承一样，对象、指针与引用的静态类型决定了我们能够使用哪些成员。

多重继承中的名字查找过程与从一个基类继承的情况下的名字查找过程类似，且名字查找先于类型检查，只是该过程会在所有直接基类中同时进行（而不是单个基类）。如果名字在多个基类中被找到，则名字的使用具有二义性。即使两个继承的函数的形参列表不同也可能发生错误；即使函数在一个类中是私有的，而在另一个类中是公有的或受保护的，也有可能发生错误。

```c++
double d = ying_yang.max_weight();  // 假设ZooAnimal（或Bear）与Endangered都定义了max_weight成员，且Pandas未定义该成员，则该调用错误。

// 为了避免潜在的二义性，最好在派生类中为该函数定义一个新版本。
double Panda::max_weight() const {
    return max(ZooAnimal::max_weight(), Endangered::max_weight());
}
```

#### 虚继承

尽管类派生列表中同一基类只能出现一次，但实际上派生类可以多次继承同一个类（通过多个中间类）。默认情况下，派生类含有继承链上每个类对应的独立的子部分，如果某个类在派生过程中出现多次，则派生类将包含该类的多个子对象。

> By default, a derived object contains a separate subpart corresponding to each class in its derivation chain. I4f the same base class appears more than once in the derivation, then the derived object will have more than one subobject of that type.

例如```istream```与```ostream```分别继承抽象基类```base_ios```（保存流的缓冲区并管理流的条件状态），```iostream```继承自```istream```与```ostream```。如果采用多重继承，```iostream```将继承```base_ios```两次，这对```iostream```类是行不通的，因为一个```iostream```对象希望在同一个缓冲区中读写，并且希望条件状态能够同时反映输入与输出操作。

在C++中通过**虚继承（virtual inheritance）**来解决这个问题。虚继承让一个类承诺它愿意共享它的基类。其中共享基类的子对象被称为**虚基类（virtual base class）**。不管虚基类在继承体系中出现多少次，派生类对象只包含唯一一个共享的虚基类子对象。

> In C++ we solve this kind of problem by using **virtual inheritance**. Virtual inheritance lets a class specify that it is willing to share its base class. The shared base-class subobject is called a **virtual base class**. Regardless of how often the same virtual base appears in an inheritance hierarchy, the derived object contains only one, shared subobject for that virtual base class.

> In practice, the requirement that an intermediate base class specify its inheritance as virtual rarely causes any problems. Ordinarily, a class hierarchy that uses virtual inheritance is designed at one time either by one individual or by a single project design group. It is exceedingly rare for a class to be developed independently that needs a virtual base in one of its base classes and in which the developer of the new base class cannot change the existing hierarchy.

> Virtual derivation affects the classes that subsequently derive from a class with a virtual base; it doesn’t affect the derived class itself.

指定虚基类的方式是在类派生列表中包含```virtual```（置于访问说明符前或后）。

```c++
// 令ZooAnimal成为Bear与Raccoon的虚基类。
class Raccoon : public virtual ZooAnimal { /* ... */ }
class Bear : virtual public ZooAnimal { /* ... */ }

// In the past, there was some debate as to whether panda belongs to the raccoon or the bear family.
class Panda : public Bear, public Raccoon, public Endangered { /* ... */ }  // 在Pandas中只有一个ZooAnimal基类部分。
```

不管基类是不是虚基类，类类型转换都成立。

```c++
void dance(const Bear&);
void rummage(const Raccoon&);
ostream &operator<<(ostream&, const ZooAnimal&);
Panda ying_yang;
dance(ying_yang);
rummage(ying_yang);
cout << ying_yang;
```

如果虚基类的成员只被一条派生路径覆盖，则可以直接访问这个被覆盖的成员且不会产生二义性（因为每个共享的虚基类中只有一个共享的子对象（there is only one shared subobject corresponding to each shared virtual base））；如果成员被多个派生路径覆盖，则通常派生类必须为该成员自定义一个版本，否则直接访问将产生二义性（当然可以通过类作用域运算符显式指定成员所属的类）。

```c++
/* 假设类B定义了成员x，D1与D2虚继承B，D继承D1与D2。通过D的对象使用x：
 * 如果D1与D2均未定义x，则x被解析为B的成员。
 * 如果x为B的成员，同时又是D1的成员，则x被解析为D1的成员。
 * 如果在D1与D2中都有x的定义，则产生二义性错误。
 */
```

在虚派生中，虚基类由最低层的派生类初始化。如果按普通的方式初始化，则虚基类可能在多条继承路径上被重复初始化。

> In a virtual derivation, the virtual base is initialized by the most derived constructor.

> As long as we can create independent objects of a type derived from a virtual base, the constructors in that class must initialize its virtual base.

```c++
// 当创建Bear时，它位于最低层，因此需要直接初始化ZooAnimal。
Bear::Bear(string name, bool onExhibit): ZooAnimal(name, onExhibit, "Bear") {}
// 当创建Raccoon时，它位于最低层，因此需要直接初始化ZooAnimal。
Raccoon::Raccoon(string name, bool onExhibit): ZooAnimal(name, onExhibit, "Raccoon") {}
```

```c++
// 当创建Panda时，它位于最低层，因此需要直接初始化ZooAnimal。
Panda::Panda(string name, bool onExhibit) : ZooAnimal(name, onExhibit, "Panda"), Bear(name, onExhibit), Raccoon(name, onExhibit), Endangered(Endangered::critical), sleeping flag(false) {}
```

含有虚基类的对象的构造顺序为：首先使用最低层派生类的构造函数的初始值初始化该对象的虚基类子部分，然后按照直接基类在派生列表中出现的次序依次对其进行初始化。虚基类先于非虚基类构造，不管它位于继承体系的何处。

> The construction order for an object with a virtual base is slightly modified from the normal order: The virtual base subparts of the object are initialized first, using initializers provided in the constructor for the most derived class. Once the virtual base subparts of the object are constructed, the direct base subparts are constructed in the order in which they appear in the derivation list.

```c++
/*
 * 当创建Panda对象时，首先使用Panda的构造函数初始值列表中的初始值构造虚基类ZooAnimal部分。
 * 然后构造Bear部分。
 * 然后构造Raccoon部分。
 * 然后构造Endangered部分。
 * 最后构造Panda部分。
 */
```

一个类可以有多个虚基类，这些虚子对象按照其在类派生列表中出现的次序从左到右构造。

```c++
/* TeddyBear对象的构造顺序：
 * ZooAnimal();
 * ToyAnimal();
 * Character();
 * BookCharacter();
 * Bear();
 * TeddyBear();
 */
class Character { /* ... */ };
class BookCharacter : public Character { /* ... */ };
class ToyAnimal { /* ... */ };
class TeddyBear : public BookCharacter, public Bear, public virtual ToyAnimal { /* ... */ };
```

合成的拷贝/移动构造函数按照相同的顺序执行，合成的赋值运算符中的成员也按该顺序赋值。和往常一样，对象销毁的顺序与构造顺序相反。

## 访问控制

通过**访问说明符（access specifier）**（后接```:```）来对类进行访问控制。同时，每个类除了控制自己的成员的初始化过程外，还控制着每个成员对于派生类来说是否**可访问（accessible）**。访问说明符包括：

- ```public```：定义在```public```后的成员可在整个程序内被访问。```public```成员定义类的接口。
- ```protected```：定义在```protected```后的成员只能被类的成员、派生类的成员与友元访问。
- ```private```：定义在```private```后的成员只能被类的成员函数与友元访问。```private```成员封装类的细节。

```c++
// 为Sales_data添加访问控制信息。
class Sales_data {
public:
    Sales_data() = default;
    Sales_data(const string &s, unsigned n, double p): bookNo(s), units_sold(n), revenue(p * n) {}
    Sales_data(const string &s): bookNo(s) {}
    Sales_data(istream&); string isbn() const {
        return bookNo;
    }
    Sales_data &combine(const Sales_data&);
private:
    double avg_price() const {
        return units_sold ? revenue/units_sold : 0;
    } 
    string bookNo; unsigned units_sold = 0; d
```

访问说明符可以以任意次序出现任意多次。

派生类的成员或友元只能通过派生类对象访问基类的```protected```成员，派生类对于基类对象中的```protected```成员没有特殊的访问权限。否则，只要定义一个基类的派生类并声明以基类为形参的函数就可以访问基类的```protected```成员了，这样就规避掉了基类的```protected```提供的保护了。

```c++
class Base {
protected:
    int prot_mem; // protected member
};
class Sneaky : public Base {
    friend void clobber(Sneaky&); // 可以访问Sneaky::prot_mem
    friend void clobber(Base&); // 不能访问Base::prot_mem
    int j;
};
void clobber(Sneaky &s) {
    s.j = s.prot_mem = 0;  // 正确。
}
void clobber(Base &b) {
    b.prot_mem = 0;  // 错误，不能访问Base的protected成员。
}
```

### 继承的访问权限

类派生列表中的派生访问说明符不会影响派生类的成员或友元对直接基类成员的访问权限，但是会影响到派生类用户（包括派生类的派生类）对继承自基类的成员的访问权限。

如果派生访问说明符是```public```的，则基类中的成员在派生类中的访问控制不变；如果派生访问说明符是```protected```的，则基类中的成员在派生类中最多是```protected```，即基类中的```public```成员在派生类中成为```protected```的；如果派生访问说明符是```private```的，则基类中的成员在派生类中全部是```private```的。

```c++
class Base {
    void pub_mem();
protected:
    int prot_mem;
private:
    char priv_mem;
};
struct Pub_Derv : public Base {
    int f() {
        return prot_mem;  // 正确。
    }
    char g() {
        return priv_mem;  // 错误，派生类无权访问基类的private成员。
    }
};
struct Priv_Derv : private Base {  // private不影响派生类的访问权限。
    int f1() const {
        return prot_mem;
    }
};

Pub_Derv d1;
Priv_Derv d2;
d1.pub_mem();  // 正确，pub_mem在派生类中是public的。
d2.pub_mem();  // 错误，pub_mem在派生类中是private的。

struct Derived_from_Public : public Pub_Derv {
    int use_base() {
        return prot_mem;  // 正确，prot_mem在Pub_Derv中是protected的。
    }
};
struct Derived_from_Private : public Priv_Derv {
    int use_base() {
        return prot_mem;  // 错误，prot_mem在Priv_Derv中是privte的。
    }
};
```

派生访问说明符对派生类向基类的转换也有影响：

- 只有当```D```公有地继承```B```时，用户代码参能使用派生类向基类的转换。
- 不论```D```以什么方式继承```B```，```D```的成员函数与友元都能使用派生类向基类的转换，派生类向直接基类的转换对于派生类的成员与友元都是可访问的。
- 如果```D```通过公有或受保护的方式继承```B```，则```D```的派生类的成员函数与友元可以使用派生类向基类的转换，否则不行。

### 继承与友元

友元关系不仅不能传递，也不能继承。基类的友元在访问派生类成员时没有特殊权限，类似地，派生类的友元在访问基类成员时也没有特殊权限。但是基类的友元可以访问基类的任何成员，包括派生类的基类部分。

```c++
class Base {
    friend class Pal;
    // ...
};
class Pal {
public:
    int f(Base b) {
        return b.prot_mem;  // 正确。
    }
    int f2(Sneaky s) {
        return s.j;  // 错误，Pal不是Sneaky的友元。
    }
    int f3(Sneaky s) {
        return s.prot_mem;  // 正确，Pal是Base的友元。注意，prot_mem是基类的成员，可以被基类的友元访问。
    }
}
```

因为友元关系不能传递，所以一个类的友元的派生类或基类不是该类的友元。

```c++
class D2 : public Pal {
public:
    int mem(Base b) {
        return b.prot_mem;  // 错误，友元关系不能继承。
    }
};
```

### 改变个别成员的可访问性

类内的```using```声明可以作用于该类的直接或间接基类中的任何可访问成员，使用```using```声明的成员的访问权限由该声明之前的类内的访问说明符决定。

```c++
class Base {
public:
    size_t size() const {
        return n;
    }
protected:
    size_t n;
};
class Derived : private Base {
public:
    using Base::size;  // 将size函数改为public的。
protected:
    using Base::n;  // 将n改为private的。
};
```

### 默认的访问控制

如果使用```class```关键字定义类，则成员的默认的访问控制权限为```private```，且默认```private```派生基类；如果使用```struct```关键字定义类，则默认的访问控制权限为```public```，且默认```public```派生基类。这也是在定义类时，关键字```class```与```struct```仅有的区别。

## 友元

类的**友元（friend）**可以访问类的所有成员。友元可以是函数、类或类的成员函数。

在类内任意位置，对函数、类或类的成员函数的声明形式前置```friend```，将其声明为该类的友元。

友元声明不是函数声明或类声明，无需先被声明，且不受访问控制的约束。当一个名字第一次出现在友元声明中时，该名字被隐式假定在当前作用域中可见，但友元本身并非真的声明在该作用域中。

最好在类定义的开头或结束位置集中声明友元。

```c++
struct X {
    friend void f();
    X() {
        f();  // 错误，f未被声明。
    }
    void g();
    void h();
};
void X::g() {
    return f();  // 错误，f未被声明。
}
void f();
void X::h() {
    return f();  // 正确。
}
```

成员函数的友元声明受到访问权限的控制，类不能将无访问权限的成员函数声明为自己的友元。

为了使友元对类的用户可见，友元的声明（函数声明或类声明）通常与对应的类放置在同一个头文件中。

每个类负责控制自己的友元，因此友元不具有传递性，即类```B```是类```A```的友元，类```C```是类```B```的友元，并不意味着类```C```是类```A```的友元。

```c++
// Sales_data的3个相关的非成员函数必须声明为Sales_data的友元。
class Sales_data {
    friend Sales_data add(const Sales_data&, const Sales_data&);
    friend istream &read(istream&, Sales_data&);
    friend ostream &print(ostream&, const Sales_data&);
    // ...
};

// 对友元的声明。
Sales_data add(const Sales_data&, const Sales_data&);
istream &read(istream&, Sales_data&);
ostream &print(ostream&, const Sales_data&);
```

```c++
/* 必须按如下方式设计程序：
 * 首先定义Windows_mgr类并声明clear函数，但是不能定义clear函数。
 * 然后定义Screen类，包括以上的友元声明。
 * 最后定义clear函数
 */

// 将Window_mgr声明为Screen的友元。
class Screen {
    friend class Window_mgr;
    friend void Window_mgr::clear(ScreenIndex);
    // ...
};

class Window_mgr {
public:
    using ScreenIndex = vector<Screen>::size_type;
    void clear(ScreenIndex);
private:
    vector<Screen> screens{Screen(24, 80, ' ')};
};

void Window_mgr::clear(ScreenIndex i) {
    Screen &s = screens[j];
    s.contents = string(s.height * s.width, ' ');
}
```

如果要为一组重载函数声明友元，则必须对该组函数中的每个函数分别声明友元（函数声明必须精确匹配）。重载函数是不同的函数，只是名字相同。

```c++
extern ostream &storeOn(ostream&, Screen&);
extern BitMap &storeOn(BitMap&, Screen&);
class Screen {
    friend ostream &storeOn(ostream&, Screen&);  // 只有第一个版本的storeOn可以访问Screen的非public成员。
    // ...
};
```

## 特殊类

### 聚合类

满足如下条件的类被称为**聚合类（aggregate class）**：

- 所有成员都是public的。
- 没有（显式）定义任何构造函数。
- 没有类内初始值。
- 没有基类，没有virtual函数。

聚合类的成员可以被直接访问。可以提供花括号括起来的初始值列表初始化聚合类的数据成员，初始值的顺序与成员声明顺序一致。

```c++
struct Data {
    int ival;
    string s;
};

Data val = {0, "Anna"};
```

如果初始值个数小于数据成员个数，则剩余的成员被值初始化。初始值个数不能超过数据成员个数。

以上的显式初始化方式存在三个明显的缺点：

- 所有成员必须都是```public```的。
- 将正确初始化成员的任务交给类的用户而非类的设计者。这样的初始化冗长乏味且易出错，因为用户很容易忘掉某个初始值或提供不合适的初始值。
- 添加或删除一个成员后，所有的初始化语句需要更新。

### 字面值常量类

字面值常量类（literal class）为字面值类型。数据成员都是字面值类型的聚合类为字面值常量类，除此以外，如果一个类满足如下条件，则其也是字面值常量类：

- 数据成员都是字面值类型。
- 至少含有一个```constexpr```构造函数。
- 如果数据成员有类内初始值，则内置成员的类内初始值必须是一条常量表达式，类类型成员的类内初始值必须使用成员自己的```constexpr```构造函数。
- 类必须使用默认定义的析构函数。

#### ```constexpr```构造函数

```constexpr```构造函数可以声明为```=default```的形式或```=delete```的形式，除此以外，```constexpr```构造函数必须满足```constexpr```函数与构造函数的所有要求。```constexpr```函数最多只能有一条返回语句（以及其他不执行任何操作的语句），而构造函数不能包含返回语句，因此```constexpr```构造函数体一般为空。

```constexpr```构造函数必须初始化所有数据成员。初始值只能使用```constexpr```构造函数或常量表达式。

```constexpr```构造函数用于生成```constexpr```对象以及```constexpr```函数的参数或返回类型。

```c++
class Debug {
public:
    constexpr Debug(bool b = true): hw(b), io(b), other(b) {}
    constexpr Debug(bool h, bool i, bool o): hw(h), io(i), other(o) {}
    constexpr bool any() {
        return hw || io || other;
    }
    void set_io(bool b) {
        io = b;
    }
    void set_hw(bool b) {
        hw = b;
    }
    void set_other(bool b) {
        hw = b;
    }
private:
 bool hw;  // 硬件（hardware）错误。
 bool io;  // IO错误。
 bool other;  // 其他错误。
};

constexpr Debug io_sub(false, true, false);  // 调试IO。
if (io_sub.any()) {
     cerr << "print appropriate error messages" << endl;
}
constexpr Debug prod(false);  // production环境下关闭调试。
if (prod.any()) {
    cerr << "print an error message" << endl;
}
```

### 嵌套类

一个类可以嵌套在另一个类的内部，前者被称为**嵌套类（nested class）**或**嵌套类型（nested type）**。嵌套类最常用于定义实现类（implementation class），例如```QueryResult```。

嵌套类是一个独立的类，并在很大程度上与外层类无关。嵌套类对象中不包含任何外层类定义的成员，外层类对象中也不包含任何嵌套类定义的成员。

嵌套类的名字在外层类的作用域中可见，在外层类作用域外不可见。嵌套类的名字不会和别的作用域中的名字冲突。

嵌套类可以包含的成员种类与非嵌套类是一样的。嵌套类也使用访问说明符外界控制成员的访问权限。外层类对嵌套类的成员没有特殊的访问权限，嵌套类对外层类的成员也没有特殊的访问权限。嵌套类本身的访问权限由外层类控制。

嵌套类必须声明在类的内部，但可以定义在类的内部或外部。如果其定义在类的外部，则需要使用作用域运算符指出其使用的外层类。

名字查找的一般规则适用于嵌套类。只是因为嵌套类本身是一个嵌套作用域，还要查找嵌套类的外层作用域。

```c++
// 将文本查询程序的QueryResult作为TextQuery的嵌套类。
class TextQuery {
public:
	class QueryResult;  // 必须先声明才能使用，特别地，必须先声明QueryResult才能声明query成员函数。
    // ...
};

class TextQuery::QueryResult {
	friend ostream &print(ostream&, const QueryResult&);
public:
	QueryResult(string s, shared_ptr<set<line_no>> p, shared_ptr<vector<string>> f) : sought(s), lines(p), file(f) {}  // 嵌套类可以直接使用外层类的成员line::no，无需作用域运算符限定。
    // ...
};

TextQuery::QueryResult TextQuery::query(const string &sought) const {
	static shared_ptr<set<line_no>> nodata(new set<line_no>);
	auto loc = wm.find(sought);
	if (loc == wm.end()) {
		return QueryResult(sought, nodata, file);
    }
	else {
        return QueryResult(sought, loc -> second, file);
    }
}

// ...
```

```c++
// 可以选择在类外定义QueryResult的构造函数：
TextQuery::TextQuery::QueryResult(string s, shared_ptr<set<TextQuery::line_no>> p, shared_ptr<vector<string>> f) : sought(s), lines(p), file(f) {}

// 假设QueryResult声明一个静态成员static_mem，则其类外定义形如：
int TextQuery::QueryResult::static_mem = 1024;
```

### 局部类

**局部类（local class）**为定义在函数内部的类。局部类定义了一个只在定义它的作用域内可见的类型。

局部类的所有成员都必须完整地定义在类的内部。因此局部类的成员函数的复杂度受到限制，一般只有几行代码，否则将很难读懂。类似地，局部类中也无法声明静态数据成员。

局部类只能访问外层作用域中定义的类型名、静态变量与枚举成员，局部类不能使用定义该局部类的函数内的普通局部变量。

```c++
int a, val;
void foo(int val) {
    static int si;
    enum Loc { a = 1024, b };
    struct Bar {
        Loc locVal;  // 正确，使用局部类型名。
        int barVal;
        void fooBar(Loc l = a) {  // 正确，默认实参为Loc::a。
            barVal = val;  // 错误，val为foo的局部变量。
            barVal = ::val;  // 正确，使用全局对象。
            barVal = si;  // 正确，si为局部静态变量。
            locVal = b;  // 正确，b为枚举成员。
        }
    };
 // . . .
}
```

常规的访问权限控制对局部类适用。外层函数对局部类的私有成员没有任何访问特权。当然，局部类可以将外层函数声明为友元。更典型的情况是，局部类将其成员声明为公有的。由于局部类已封装在函数作用域中，通过信息隐藏进行进一步通常没有必要。

局部类内部的名字查找方式与其他类类似。

局部类中可以嵌套类。此时，嵌套类的定义可以出现在局部类外，但必须在与局部类相同的作用域中。局部类内的嵌套类也是局部类，遵循局部类的各种规定，例如，嵌套类的所有成员都必须定义在该嵌套类内部。

```c++
void foo() {
    class Bar {
    public:
        // ...
        class Nested;
    };
    class Bar::Nested {  // 和往常一样，必须指明该成员所属的作用域。
        // ...
    };
}
```

### ```union```

```union```是一种特殊的类。一个```union```可以有多个数据成员，但是在任意时刻只有一个数据成员有值。当一个数据成员被赋值后，其他成员都变成未定义的了。分配给一个```union```的存储空间至少要能容纳下它的最大的数据成员。和其他类一样，一个```union```定义了一种新类型。

```union```不能含有引用类型成员，除此以外，它的成员可以是绝大多数类型，包括含有构造函数或析构函数的类类型。```union```可以为其成员指定```public```、```protected```与```private```访问说明符。默认情况下，```union```的成员都是公有的。

```union```可以定义包括构造函数与析构函数在内的成员函数，但是```union```不能继承自其他类，也不能作为基类，因此不能有虚函数。

```union```的定义与普通类类似，只是```struct```或```class```关键字被```union```替代。

```union```提供了一种有效途径，使得可以方便地表示一组类型不同的互斥值。

```c++
union Token {
    char cval;
    int ival;
    double dval;
};
```

默认情况下，```union```未被初始化。可以像显式初始化聚合类那样初始化```union```对象，此时提供的初始值用于初始化第一个成员。

```c++
Token first_token = {'a'};  // 初始化cval成员。
Token last_token;
Token *pt = new Token;  // pt指向未初始化的Token。
```

可以使用成员访问运算符访问```union```对象的成员。

```c++
// 为一个数据成员赋值会导致其他成员处于未定义状态。
// 取决于成员类型，通过错误的数据成员访问union存储的值或为union存储的值赋值会导致程序崩溃或异常。
last_token.cval = 'z';
p -> ival = 42;
```

#### 匿名union

**匿名```union```（anonymous ```union```）**是一个未命名的```union```，一旦定义了一个匿名```union```，编译器就自动为该```union```创建一个未命名的对象。匿名```union```的成员在定义该```union```所在的作用域内可被直接访问。

```c++
union {
    char cval;
    int ival;
    double dval;
};
cval = 'c';  // 为匿名union对象赋值。
ival = 42;  // 该对象当前保存的值是42。
```

匿名```union```不能包含```private```或```protected```成员，也不能定义成员函数。

#### 含有类类型成员的```union```

当```union```包含的是内置类型的成员时，可以使用普通的赋值语句改变```union```保存的值；当将```union```的值改为类类型成员对应的值时，必须运行该类型的构造函数；当将类类型成员的值改为其他值时，必须运行该类型的析构函数。

> When a ```union``` has members of built-in type, we can use ordinary assignment to change the value that the ```union``` holds. Not so for ```union```s that have members of nontrivial class types. When we switch the ```union```’s value to and from a member of class type, we must construct or destroy that member, respectively: When we switch the ```union``` to a member of class type, we must run a constructor for that member’s type; when we switch from that member, we must run its destructor.

当```union```包含内置类型的成员，编译器按照成员的次序依次合成默认构造函数或拷贝控制成员；如果一个```union```含有类类型的成员，且其定义了默认构造函数或拷贝控制成员，则编译器将为```union```合成对应的版本并将其声明为删除的。

> When a ```union``` has members of built-in type, the compiler will synthesize the memberwise versions of the default constructor or copy-control members. The same is not true for ```union```s that have members of a class type that defines its own default constructor or one or more of the copy-control members. If a ```union``` member’s type defines one of these members, the compiler synthesizes the corresponding member of the ```union``` as deleted (§ 13.1.6, p. 508).

例如，```string```定义了五个拷贝控制成员以及一个默认构造函数。如果```union```包含一个```string```且没有定义默认构造函数或某个拷贝控制成员，则编译器将合成缺少的成员并将其声明为删除的。如果类含有一个```union```成员，并且该```union```含有删除的拷贝控制成员，则该类与之对应的拷贝控制操作也是删除的。

由于```union```构造或销毁类类型的成员很复杂，因此含有类类型成员的```union```通常内嵌在其他类内部。这个类可以管理```union```类类型成员与其他成员的状态转换。

> Because of the complexities involved in constructing and destroying members of class type, ```unions``` with class-type members ordinarily are embedded inside another class. That way the class can manage the state transitions to and from the member of class type.

```c++
class Token {
public:
    Token(): tok(INT), ival{0} {}
    Token(const Token &t): tok(t.tok) {
        copyUnion(t);
    }
    Token &operator=(const Token&);
    ~Token() {
        if (tok == STR) {
            sval.~string();
        }
    }
    Token &operator=(const string&);
    Token &operator=(char);
    Token &operator=(int);
    Token &operator=(double);
private:
    enum { INT, CHAR, DBL, STR } tok;  // 判别式（discriminant）。A discriminant lets us discriminate among the values that the union can hold.
    union {
        char cval;
        int ival;
        double dval;
        string sval;
    };
};

Token &Token::operator=(char c) {
    if (tok == STR) {
        sval.~string();
    }
    cval = c;
    tok = CHAR;
    return *this;
}

Token &Token::operator=(int i) {
    if (tok == STR) {
        sval.~string();
    }
    ival = i;
    tok = INT;
    return *this;
}

Token &Token::operator=(double d) {
    if (tok == STR) {
        sval.~string();
    }
    dval = d;
    tok = DBL;
    return *this;
}

Token &Token::operator=(const string &s) {
    if (tok == STR) {
        sval = s;
    } else {
        new(&sval) string(s);
    }
    tok = STR;
    return *this;
}

void Token::copyUnion(const Token &t) {
    switch (t.tok) {
        case Token::INT:
            ival = t.ival;
            break;
        case Token::CHAR:
            cval = t.cval;
            break;
        case Token::DBL:
            dval = t.dval;
            break;
        case Token::STR:
            new(&sval) string(t.sval);
            break;
    }
}

Token &Token::operator=(const Token &t) {
    if (tok == STR && t.tok != STR) {
        sval.~string();
    }
    if (tok == STR && t.tok == STR) {
        sval = t.sval;
    } else {
        copyUnion(t);
    }
    tok = t.tok;
    return *this;
}
```

# 输入输出（IO）

## 运算

通过```>>```运算符从```istream```对象中读取数据，运算符左侧运算对象为```istream```对象，右侧运算对象为读取的数据。

通过```<<```运算符向```ostream```对象中写入数据，运算符左侧运算对象为```ostream```对象，右侧运算对象为写入的数据。

```iostream```同时支持以上两种运算符与对应的操作。

```getline```函数位于头文件```string```中，用于从给定的```istream```（第一个实参）中读取一行数据，存入给定的```string```（第二个实参）对象中。

IO对象不支持拷贝或赋值操作。

```c++
ofstream out1, out2;
out1 = out2;  // 错误。
ostream print(ostream);
out2 = print(out2);  // 错误。
```

## 条件状态

流可通过以下函数或标志访问与操纵流的**条件状态（condition state）**，其中```strm```为IO类，```s```为IO对象。

<table>
    <tr>
        <td>strm::iostate</td>
        <td>机器相关的整型，标识流的条件状态，被当作位集合使用。</td>
    </tr>
    <tr>
        <td>strm::badbit</td>
        <td>strm::iostate类型的constexpr值，指示流崩溃，表示系统级错误，如果badbit被置位，failbit也被置位。通常一旦badbit被置位，则流就再也无法使用了。</td>
    </tr>
    <tr>
        <td>strm::failbit</td>
        <td>strm::iostate类型的constexpr值，指示IO操作失败，表示可恢复的错误。</td>
    </tr>
    <tr>
        <td>strm::eofbit</td>
        <td>strm::iostate类型的constexpr值，指示流到达了end-of-file（文件尾）。如果流到达文件尾，则failbit也被置位。</td>
    </tr>
    <tr>
        <td>strm::goodbit</td>
        <td>strm::iostate类型的constexpr值，指示流处于非错误状态，该值保证为0。</td>
    </tr>
    <tr>
        <td>s.eof()</td>
        <td>当且仅当s设置了eofbit时返回true。</td>
    </tr>
    <tr>
        <td>s.fail()</td>
        <td>当且仅当s设置了failbit或badbit时返回true。</td>
    </tr>
    <tr>
        <td>s.bad()</td>
        <td>当且仅当s设置了badbit时返回true。</td>
    </tr>
    <tr>
        <td>s.good()</td>
        <td>当且仅当s处于有效状态时返回true。</td>
    </tr>
    <tr>
        <td>s.clear()</td>
        <td>将s中所有条件值设置为有效，返回void。</td>
    </tr>
    <tr>
        <td>s.clear(flags)</td>
        <td>复位s的flags，flags的类型为strm::iostate，返回void。</td>
    </tr>
    <tr>
        <td>s.setstate(flags)</td>
        <td>置位s的flags，flags的类型为strm::iostate，返回void。</td>
    </tr>
    <tr>
        <td>s.rdstate()</td>
        <td>返回s当前状态，返回类型为strm::iostate。</td>
    </tr>
</table>

如果流的```badbit```、```failbit```或```eofbit```被置位，则流处于错误状态。确定流的总体状态的正确方法是使用```good```或```fail```。

```c++
int ival;
cin >> ival;  // 如果在标准输入中键入“Boo”或输入end-of-file，则cin进入错误状态。
```

流一旦发生错误，则后续的IO操作都会失败。只有当流处于无错状态时，才可以读写数据。代码通常应该在使用一个流前检查其是否处于良好状态。确定一个流对象是否处于无错状态的最简单的方法是将其当作一个条件来使用，这等价于调用```!s.fail()```。

```c++
while (cin >> word) {  // 如果流无错，则条件为true，否则为false。
    // ...
}
```

```c++
auto old_state = cin.rdstate();  // 记住cin当前状态。
cin.clear();  // 使cin有效。
process_input(cin);  // 使用cin。
cin.setstate(old_state);  // 将cin置为原来状态。
```

```c++
cin.clear(cin.rdstate() & ~cin.failbit & ~cin.badbit);  // 复位cin的failbit与badbit，保持其他状态位不变。
```

## 输出缓冲管理

每个输出流都管理一个缓冲区，用来保存程序读写的数据。

```c++
cout << "please enter a value: ";  // 文本串可能立即打印出来，也可能被操作系统保存在缓冲区中。
```

以下情况会导致缓冲区刷新（数据输出到实际设备或文件中）：

- 程序正常结束。
- 缓冲区满。此时新数据到来前缓冲区被刷新。
- 使用操纵符如```endl```显式刷新缓冲区。
- 使用```unitbuf```操纵符设置流的内部状态，使得在每个输出操作后清空缓冲区。默认情况下```cerr```设置了```unitbuf```。
- 一个输出流可能被关联到另一个流。此时当读写关联的流时，关联到的流的缓冲区被刷新。默认情况下，```cin```与```cerr```被关联到```cout```，因此读```cin```或写```cerr```会刷新```cout```的缓冲区。

```c++
cout << "hi!" << endl;  // 输出“hi!”与换行，然后刷新缓冲区。
cout << "hi!" << flush;  // 输出“hi!”，然后刷新缓冲区。
cout << "hi!" << ends;  // 输出“hi!”与空字符，然后刷新缓冲区。
```

```c++
cout << unitbuf;  // 所有cout输出操作后都会立即其刷新缓冲区。
cout << nounitbuf; // 回到正常缓冲方式。
```

如果程序崩溃，则输出缓冲区不会被刷新，此时输出数据可能仍停留在输出缓冲区等待打印。在调试已崩溃程序时，需要注意这一点。

通过流对象的```tie```函数查询或设置流的关联信息。```tie```有两个重载版本，一个版本无参数，返回指向关联该流的输出流的指针（如果有的话）；一个版本接受一个指向```ostream```指针形参，将流关联到该输出流，并返回原来关联的流。可以将一个```istream```对象或```ostream```对象关联到另一个```ostream```。每个流同时最多关联到一个流，但多个流可以同时关联到同一个```ostream```。

> There are two overloaded (§ 6.4, p. 230) versions of  ```tie```: One version takes no argument and returns a pointer to the output stream, if any, to which this object is currently tied. The function returns the null pointer if the stream is not tied.
>
> The second version of ```tie``` takes a pointer to an ```ostream``` and ties itself to that ```ostream```. That is, ```x.tie(&o)``` ties the stream ```x``` to the output stream ```o```.

> We can tie either an ```istream``` or an ```ostream``` object to another ```ostream```:

> Each stream can be tied to at most one stream at a time. However, multiple streams can tie themselves to the same ```ostream```.

```c++
cin.tie(&cout);  // 将cin与cout关联在一起，默认情况下即如此。
ostream *old_tie = cin.tie(nullptr);  // cin不再与其他流关联。
cin.tie(&cerr);  // 将cin与cerr关联，这不是一个好主意。
cin.tie(old_tie);  // 重建cin与cout间的关联。
```

一般交互式系统应该关联输入流和输出流，以使得所有输出在输入操作前打印出来。

## IO类型

标准库的IO类型包括：

<table>
    <tr align="center">
        <th rowspan="2">头文件</th> 
        <th colspan="2">类型</th>
        <th rowspan="2">功能</th>
    </tr>
    <tr>
        <th>操纵char的版本</th> 
        <th>操纵wchar_t的版本</th>
    </tr>
    <tr>
    </tr>
    <tr>
        <td rowspan="3">iostream</td>
        <td>istream</td>
        <td>wistream</td>
        <td>从流中读取数据</td>
    </tr>
    <tr>
        <td>ostream</td>
        <td>wostream</td>
        <td>向流中写入数据</td>
    </tr>
    <tr>
        <td>iostream</td>
        <td>wiostream</td>
        <td>读写流，继承自istream与ostream。</td>
    </tr>
    <tr>
        <td rowspan="3">fstream</td>
        <td>ifstream</td>
        <td>wifstream</td>
        <td>从文件中读取数据，继承自istream。</td>
    </tr>
    <tr>
        <td>ofstream</td>
        <td>wofstream</td>
        <td>向文件中写入数据，继承自ostream。</td>
    </tr>
    <tr>
        <td>fstream</td>
        <td>wfstream</td>
        <td>读写文件，继承自iostream。</td>
    </tr>
    <tr>
        <td rowspan="3">sstream</td>
        <td>istringstream</td>
        <td>ostringstream</td>
        <td>从string中读取数据，继承自istream。</td>
    </tr>
    <tr>
        <td>ostringstream</td>
        <td>wostringstream</td>
        <td>向string中写入数据，继承自ostream。</td>
    </tr>
    <tr>
        <td>stringstream</td>
        <td>wstringstream</td>
        <td>读写string，继承自iostream。</td>
    </tr>
</table>

### 文件IO

标准库定义了三种类型支持文件IO：```ifstream```、```ofstream```与```fstream```。除了继承自```iostream```的功能外，其还支持以下操作，其中```fstream```表示文件IO类，```fstrm```表示文件IO对象：

<table>
    <tr>
        <td>fstream fstrm</td>
    	<td>创建一个未绑定的文件流，随后可以使用open函数将其与文件关联起来。</td>
    </tr>
    <tr>
        <td>fstream fstrm(s)</td>
        <td>创建一个文件流，打开名为s的文件并与该文件流绑定（open函数被自动调用）。s为string或指向C风格字符串的指针。构造函数为explicit的。</td>
    </tr>
    <tr>
        <td>fstream fstrm(s, mode)</td>
        <td>等价于fstream fstrm(s)，但是指定文件模式为mode。</td>
    </tr>
    <tr>
        <td>fstrm.open(s)</td>
        <td>打开名为s的文件并与fstrm绑定。s为string或指向C风格字符串的指针。如果open失败，则failbit被置位。</td>
    </tr>
    <tr>
        <td>fstrm.close()</td>
        <td>关闭与fstrm绑定的文件，返回void。</td>
    </tr>
    <tr>
        <td>fstrm.is_open()</td>
        <td>指示与fstrm关联的文件是否成功打开且未关闭。</td>
    </tr>
</table>
```c++
istream in(ifile);  // 构建一个i发stream打开名为ifile的文件。
ofstream out;
out.open(ifile + ".copy");  // 通过open打开指定文件。
if (out) {  // 每次检测open是否成功是一个好习惯。
    // ...
}
```

```c++
// Sales_data的一种典型使用。
// 使用文件流重写。
ifstream input(argv[1]);  // argv为main函数的第2个参数。
ofstream output(argv[2]);
Sales_data total;
if (read(input, total)) {
    Sales_data trans;
    while(read(input, trans)) {
        if (total.isbn() == trans.isbn()) {
            total.combine(trans);
        } else {
            print(output, total) << endl;
            total = trans;
        }
    }
    print(output, total) << endl;
} else {
     cerr << "No data?!" << endl;
}
```

一旦文件流被打开，其保持与对应文件的关联直到调用```close```函数。对一个已经打开的文件流调用```open```会失败，并导致```failbit```被置位，随后试图使用该文件流的操作会失败。

```c++
in.close();
in.open(ifile + "2");  // 关闭in后才能重新打开另一个文件。
```

当一个文件流对象离开其作用域时（即使仍在生命周期内），与之关联的文件会自动关闭。

```c++
for (auto p = argv + 1; p != argv + argc; ++p) {  // argv与argc为main函数的两个参数。
    ifstream input(*p);
    if (input) {
        process(input);
    } else {
        cerr << "couldn't open: " + string(*p);
    }
    // 不用显式关闭文件流，因为input离开作用域后自动被销毁。
}
```

#### 文件模式

每个文件流关联的**文件模式（file mode）**指示如何使用文件。文件模式包括，其中```fstream```表示文件IO类：

<table>
    <tr>
        <td>fstream::in</td>
        <td>以读方式打开文件。</td>
    </tr>
    <tr>
        <td>fstream::out</td>
        <td>以写方式打开文件。</td>
    </tr>
    <tr>
        <td>fstream::app</td>
        <td>每次写操作前定位到文件末尾。</td>
    </tr>
    <tr>
        <td>fstream::ate</td>
        <td>打开文件后立即定位到文件末尾。</td>
    </tr>
    <tr>
        <td>fstream::trunc</td>
        <td>截断文件。</td>
    </tr>
    <tr>
        <td>fstream::binary</td>
        <td>以二进制方式进行IO操作。</td>
    </tr>
</table>
文件模式使用类作用域运算符加以限制，以指定其属于哪个文件流类型。文件模式被当作位集合使用。

指定文件模式时有以下规则：

- 只能对```ifstream```或```fstream```设置```in```模式。

- 只能对```ofstream```或```fstream```设置```out```模式。
- 只有当```out```模式被设置时才能设置```trunc```模式。
- 当且仅当```trunc```模式未被设置时可以设置```app```模式。如果```app```模式被设置，则文件总是以输出模式打开，即使未显式设置```out```模式。
- 默认情况下，即使未指定```trunc```模式，以```out```模式打开的文件也会被截断。可以同时设置```out```模式与```app```模式以将数据追写到文件末尾，或同时设置```out```模式与```in```模式打开文件进行读写操作以防止文件被截断。
- ```ate```与```binary```模式可用于任何类型的文件流对象，且可与其他任何文件模式组合使用。
- 每个文件流定义了默认的文件模式。与```ifstream```关联的文件默认以```in```模式打开，与```ofstream```关联的文件默认以```out```模式打开，与```fstream```关联的文件流默认以```in```与```out```模式打开。

```c++
// 以下三条语句会截断file1。
ofstream out("file1");
ofstream out2("file1", ofstream::out);
ofstream out3("file1", ofstream::out | ofstream::trunc);

// 以下两条语句保留file2的内容。
ofstream app("file2", ofstream::app);
ofstream app2("file2", ofstream::out | ofstream::app);
```

每次打开文件（调用```open```函数）时都会重新确定文件模式。

```c++
ofstream out;
out.open("scratchpad");  // 模式为out与trunc。
out.close();
out.open("precious", ofstream::app);  // 模式为out与app。
out.close();
```

### ```string```流

标准库定义了三种类型支持内存IO：```istringstream```、```ostringstream```与```stringstream```。除了继承自```iostream```的功能外，其还支持以下操作，其中```sstream```表示内存IO类，```strm```表示内存IO对象：

<table>
    <tr>
        <td>sstream strm</td>
        <td>创建一个未绑定的string流。</td>
    </tr>
    <tr>
        <td>sstream strm(s)</td>
        <td>创建一个的string流，保存string对象s的拷贝。构造函数为explicit的。</td>
    </tr>
    <tr>
        <td>strm.str()</td>
        <td>返回strm保存的string的拷贝。</td>
    </tr>
    <tr>
        <td>strm.str(s)</td>
        <td>将string对象s拷贝到strm中，返回void。</td>
    </tr>
</table>

```c++
/* 假设有一个文件列出了一些人与他们的电话号码集合。文件中包括多条记录，每条记录占一行，包括多个以空格分割的项，第一个项为人名，后面为该人所有的电话号码。类似如下：
 *     morgan 2015552368 8625550123
 *     drew 9735550130
 *     lee 6095550132 2015550175 8005550000
 */

// 描述输入数据的类。
struct PersonInfo {
    string name;  // 人名。
    vector<string> phones;  // 该人所有的电话号码。
};

// 使用istringstream解析文件。
string line, word;
vector<PersonInfo> people;
while (getline(in, line)) {  // in为文件输入流。
    PersonInfo info;
    istringstream record(line);
    record >> info.name;
    while (record >> word) {
        info.phones.push_back(word);
    }
    people.push_back(info);
}

// 使用ostringstream打印结果。
for (const auto &entry : people) {
    ostringstream formatted, badNums;
    for (const auto &nums : entry.phones) {
        if (!valid(nums)) {  // 验证电话号码是否合法。
            badNums << " " << nums;
        } else {
            formatted << " " << format(nums);  // 使用format格式化输出。
        }
    if (badNums.str().empty()) {
        os << entry.name << " " << formatted.str() << endl;
    } else {
         cerr << "input error: " << entry.name << " invalid number(s) " << badNums.str() << endl;
    }
}
```

### 标准库IO对象

标准库定义了四个IO对象，这些对象位于```iostream```头文件中：

<table>
    <tr>
        <td>cin</td>
        <td>标准输入（standard input），为istream对象。</td>
    </tr>
    <tr>
        <td>cout</td>
        <td>标准输出（standard output），为ostream对象。</td>
    </tr>
    <tr>
        <td>cerr</td>
        <td>标准错误（standard error），用于输出警告与错误消息，为ostream对象。</td>
    </tr>
    <tr>
        <td>clog</td>
        <td>用于输出程序运行时的一般性信息，为ostream对象。</td>
    </tr>
</table>
## 格式化输入输出

**格式化输入输出（formatted IO）**使用```>>```与```<<```运算符，根据读取或写入的数据的类型来格式化它们。

每个```iostream```对象维护一个格式状态来控制IO格式化的细节。

标准库定义了一组**操纵符（manipulator）**来修改流的格式状态，一个操纵符可以是一个函数或一个对象，会影响流的状态，并能用作输入或输出运算符的运算对象。操纵符返回它所处理的流对象，因此可以在一条语句中组合操纵符与数据。这些操纵符定义在头文件```iostream```中，如下：

<table>
    <tr>
        <th>操纵符</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>boolalpha</td>
        <td>将true与false输出为字符串。</td>
    </tr>
    <tr>
        <td>noboolalpha</td>
        <td>默认状态，将true与false输出为0或1。</td>
    </tr>
    <tr>
        <td>showbase</td>
        <td>对整型值生成表示进制的前缀。</td>
    </tr>
    <tr>
        <td>noshowbase</td>
        <td>默认状态，对整型值不生成表示进制的前缀。</td>
    </tr>
    <tr>
        <td>showpoint</td>
        <td>对浮点值总是显示小数点。</td>
    </tr>
    <tr>
        <td>noshowpoint</td>
        <td>默认状态，只有当浮点值包含小数部分才显示小数点。</td>
    </tr>
    <tr>
        <td>showpos</td>
        <td>对非负数显示+。</td>
    </tr>
    <tr>
        <td>noshowpos</td>
        <td>默认状态，对非负数不显示+。</td>
    </tr>
    <tr>
        <td>uppercase</td>
        <td>十六进制中打印0X，科学计数法中打印E。</td>
    </tr>
    <tr>
        <td>nouppercase</td>
        <td>默认状态，十六进制中打印0x，科学计数法中打印e。</td>
    </tr>
    <tr>
        <td>dec</td>
        <td>默认状态，整型采用十进制。</td>
    </tr>
    <tr>
        <td>hex</td>
        <td>整型采用十六进制。</td>
    </tr>
    <tr>
        <td>oct</td>
        <td>整型采用八进制。</td>
    </tr>
    <tr>
        <td>left</td>
        <td>在值的右侧填充字符（左对齐）。</td>
    </tr>
    <tr>
        <td>right</td>
        <td>在值的左侧填充字符（右对齐）。</td>
    </tr>
    <tr>
        <td>internal</td>
        <td>在符号与值之间填充字符。</td>
    </tr>
    <tr>
        <td>fixed</td>
        <td>浮点值采用定点十进制。</td>
    </tr>
    <tr>
        <td>scientific</td>
        <td>浮点值采用科学计数法。</td>
    </tr>
    <tr>
        <td>hexfloat</td>
        <td>浮点值采用十六进制。</td>
    </tr>
    <tr>
        <td>defaultfloat</td>
        <td>重置浮点数格式为十进制。</td>
    </tr>
    <tr>
        <td>unitbuf</td>
        <td>每次输出操作后刷新缓冲区。</td>
    </tr>
    <tr>
        <td>nounitbuf</td>
        <td>默认状态，恢复正常的缓冲区刷新方式。</td>
    </tr>
    <tr>
        <td>skipws</td>
        <td>默认状态，跳过输入运算符中的空白字符。</td>
    </tr>
    <tr>
        <td>noskipws</td>
        <td>取消跳过输入运算符中的空白字符（包括换行）。</td>
    </tr>
    <tr>
        <td>flush</td>
        <td>刷新ostream缓冲区。</td>
    </tr>
    <tr>
        <td>ends</td>
        <td>插入空字符，然后刷新ostream缓冲区。</td>
    </tr>
    <tr>
        <td>endl</td>
        <td>插入换行，然后刷新ostream缓冲区。</td>
    </tr>
</table>
操纵符用于两大类输出控制：控制值的输出形式、控制填充（padding）的数量与位置。大多数改变格式状态的操纵符都是成对的：一个设置格式状态为新值，一个恢复正常的、默认的格式。当操纵符改变流的格式状态时，通常改变后的状态对后续所有IO都生效。有时候这一特性很有用，但很多程序或程序员期望流的状态符合默认设置，因此通常最好在不再需要特殊格式时尽快将流恢复到默认状态。

流通过IO对象的```precision```成员或```setprecision```操纵符来改变浮点数的精度。```precision```一个版本接受一个```int```值，将精度设置为此值，另一个版本不接受参数，返回当前精度值。```setprecision```接受一个参数，用来设置精度。

```c++
cout << "Precision: " << cout.precision() << ", Value: " << sqrt(2.0) << endl;
cout.precision(12);
cout << "Precision: " << cout.precision() << ", Value: " << sqrt(2.0) << endl;
cout << setprecision(3);
cout << "Precision: " << cout.precision() << ", Value: " << sqrt(2.0) << endl;
```

标准库头文件```iomanip```提供一些操纵符来控制数据格式（包括上文的```setprecision```等接受实参的操纵符）：

<table>
    <tr>
        <th>操纵符</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>setfill(ch)</td>
        <td>用ch填充空白。</td>
    </tr>
    <tr>
        <td>setprecision(n)</td>
        <td>将浮点精度设置为n。</td>
    </tr>
    <tr>
        <td>setw(w)</td>
        <td>读或写值的宽度为w个字符。</td>
    </tr>
    <tr>
        <td>setbase(b)</td>
        <td>整型采用b进制。</td>
    </tr>
</table>

```c++
int i = -16;
double d = 3.14159;

cout << "i: " << setw(12) << i << "next col" << '\n'
     << "d: " << setw(12) << d << "next col" << '\n';

cout << left
     << "i: " << setw(12) << i << "next col" << '\n'
     << "d: " << setw(12) << d << "next col" << '\n'
     << right;

cout << right
     << "i: " << setw(12) << i << "next col" << '\n'
     << "d: " << setw(12) << d << "next col" << '\n';

cout << internal
     << "i: " << setw(12) << i << "next col" << '\n'
     << "d: " << setw(12) << d << "next col" << '\n';

cout << setfill('#')
     << "i: " << setw(12) << i << "next col" << '\n' << "d: " << setw(12) << d      << "next col" << '\n'
     << setfill(' ');
```

## 未格式化输入输出

**未格式化输入输出（unformatted IO）**将流当作一个未解释的字节序列来处理。

单字节的未格式化输入输出操作如下，其中```is```为```istream```，```os```为```ostream```，```ch```为字符：

<table>
    <tr>
        <th>操纵符</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is.get(ch)</td>
        <td>从is中读取下一个字节存入ch中，返回is。</td>
    </tr>
    <tr>
        <td>os.put(ch)</td>
        <td>将ch输出到os中，返回os。</td>
    </tr>
    <tr>
        <td>is.get()</td>
        <td>将is的下一个字节作为int返回。</td>
    </tr>
    <tr>
        <td>is.putback(ch)</td>
        <td>将ch放回is，返回is。ch必须与最后读取的值相同。</td>
    </tr>
    <tr>
        <td>is.unget()</td>
        <td>将is向后移动一个字节，返回is。这导致最后读取的值又回到流中。</td>
    </tr>
    <tr>
        <td>is.peek()</td>
        <td>将is中下一个字节作为int返回，但不从流中删除它。</td>
    </tr>
</table>
一些单字节的未格式化操作返回```int```而不是```char```。这样程序可以返回文件end-of-file标记。这些函数首先将其要返回的字符转换为```unsigned char```，然后将结果提升到```int```，因此字符的返回值一定是正值。而标准库使用负值表示end-of-file，保证其与任何合法字符的值都不同。头文件```cstdio```定义了一个名为```EOF```的```const```，可以用它来检测从```get```返回的值是否是end-of-file。

```c++
int ch;
while ((ch = cin.get) != EOF) {
    cout.put(ch);
}
```

多字节的未格式化输入输出操作如下，其中```is```为```istream```，```os```为```ostream```，```ch```为字符：

<table>
    <tr>
        <th>操纵符</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is.get(sink, size, delim)</td>
        <td>从is中读取字符，并保存在字符数组中，字符数组的起始地址由sink给出。当读取过程中遇到字符delim、读取了size个字节或遇到end-of-file时，读取结束。如果遇到了delim，则将其留在is中，不会读取出来存入sink。</td>
    </tr>
    <tr>
        <td>is.getline(sink, size, delim)</td>
        <td>类似is.get(sink, size, delim)，但会读取并丢弃delim。</td>
    </tr>
    <tr>
        <td>is.read(sink, size)</td>
        <td>读取最多size个字节，存入字符数组sink中，返回is。</td>
    </tr>
    <tr>
        <td>is.gcount()</td>
        <td>返回上一个未格式化读取操作从is中读取的字节数。应该在任何后续未格式化操作前调用gcount。特别地，将字符退回流的单字符操作也属于未格式化操作。如果在调用gcount前调用了peek、unget或putback，则其返回0。</td>
    </tr>
    <tr>
        <td>os.write(source, size)</td>
        <td>从字符数组source中读取size个字节到os中，返回os。</td>
    </tr>
    <tr>
        <td>is.ignore(size, delim)</td>
        <td>读取并忽略最多size个字符，直到遇到并包括delim。size默认为1，delim默认为end-of-file。</td>
    </tr>
</table>
未格式化操作为低层程序，很容易出错。一个常见的编写错误是将```get```或```peek```的返回值赋予一个```char```而不是一个```int```。因此尽量避免使用未格式化操作。

```c++
/* 将get的返回值赋予char是错误的编程实现。
 * 如果机器将char实现为unsigned char，则循环永远不会终止。因为get的返回值被转换为unsigned char，与EOF不可能相等。
 * 如果机器将char实现为signed char，则循环的行为取决于编译器。在很多机器上，这个循环可以正常工作。但如果输入序列中有一个字符（例如'\377'）的值等于EOF，则循环会提前终止，这通常发生在读取二进制值的场合中。
 */
char ch;
while ((ch = cin.get()) != EOF) {
    cout.put(ch);
}
```

## 流随机访问

标准库为所有的流定义了```seek```与```tell```函数，用于重定位流，其具体实现如下：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>tellg()</td>
        <td>返回输入流中标记（marker）的当前位置。</td>
    </tr>
    <tr>
        <td>tellp()</td>
        <td>返回输出流中标记（marker）的当前位置。</td>
    </tr>
    <tr>
        <td>seekg(pos)</td>
        <td>将输入流中的标记重定位到绝对地址pos处。pos通常是前一个tellg返回的值。</td>
    </tr>
    <tr>
        <td>seekp(pos)</td>
        <td>将输出流中的标记重定位到绝对地址pos处。pos通常是前一个tellp返回的值。</td>
    </tr>
    <tr>
        <td>seekp(off, from)</td>
        <td>将输入流中的标记重定位到from之前或之后的off个字符处。</td>
    </tr>
    <tr>
        <td>seekg(off, from)</td>
        <td>将输出流中的标记重定位到from之前或之后的off个字符处。</td>
    </tr>
</table>

其中```from```的取值为，其中```stream```为对应的流类型：

<table>
    <tr>
        <th>取值</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>stream::beg</td>
        <td>偏移量相对于流开始位置。</td>
    </tr>
    <tr>
        <td>stream::cur</td>
        <td>偏移量相对于流当前位置。</td>
    </tr>
    <tr>
        <td>stream::end</td>
        <td>偏移量相对于流结尾位置。</td>
    </tr>
</table>

流随机访问本质上依赖于系统。

虽然标准库为所有流类型都定义了以上函数，但它们是否会做有意义的事取决于流被绑定到哪个设备。在大多数系统中，绑定到```cin```、```cout```、```cerr```与```clog```的流不支持随机访问。可以对这些流调用以上函数，但会运行时错误，且将流置于无效状态。通常，```istream```与```ostream```类型不支持流随机访问。

逻辑上，只能对输入流（```istream```及其子类）调用```g```版本的函数，只能对输出流（```ostream```及其子类）调用```p```版本的函数，否则编译器报错；```fstream```与```stringstream```同时能使用```g```与```p```版本的函数。

尽管标准库分别使用```g```与```p```版本的函数操作流随机访问，但是标准库只在流中维护单一的标记，不存在独立的读标记与写标记。这在```fstream```与```stringstream```读写时尤其需要注意，这些类型只有单一的缓冲区保存读写数据，同样也只有一个标记表示缓冲区当前位置，```g```与```p```版本的读写位置都映射到这一单一标记。

```c++
// 在文件末尾写入新的一行，包含文件中每行相对起始的位置。
fstream inOut("copyOut", fstream::ate | fstream::in | fstream::out);
if (!inOut) {
    cerr << "Unable to open file!" << endl;
    return EXIT_FAILURE;
}
auto end_mark = inOut.tellg();  // 原文件尾位置。
inOut.seekg(0, fstream::beg);
size_t cnt = 0;
string line;
while (inOut && inOut.tellg() != end_mark && getline(inOut, line)) {
    cnt += line.size() + 1;
    auto mark = inOut.tellg();
    inOut.seekp(0, fstream::end);
    inOut << cnt;
    if (mark != end_mark) inOut << " ";
    inOut.seekg(mark);
}
inOut.seekp(0, fstream::end);
inOut << "\n";
```

# 迭代器

如果一种类型支持访问容器的元素且从某个元素移动到另外一个元素，则其可被称为**迭代器（iterator）**。迭代器可泛指迭代器概念本身、容器定义的迭代器类型或某个迭代器对象。

## 种类

### 输入迭代器

**输入迭代器（input iterator）**只能读取序列中的元素，其必须支持以下操作（其中```iter```、```iter1```与```iter2```为输入迭代器）：

- 比较两个迭代器是否相等：```iter1 ==  iter2```；```iter1 != iter2```。
- 令迭代器指向下一个元素：```iter++```；```++iter```。
- 读取迭代器指向的元素：```*iter```。```*iter```只会出现在赋值运算符右侧。
- 解引用迭代器并提取其指向的元素的成员```member```：```iter -> member```。

对于输入迭代器，```*iter++```保证是有效的，但是该操作可能导致流中的所有其他迭代器失效，因此不能保证输入迭代器的状态可被保存下来并用来访问元素，因此输入迭代器只能用于单遍扫描算法。

> Input iterators may be used only sequentially. We are guaranteed that ```*it++``` is valid, but incrementing an input iterator may invalidate all other iterators into the stream. As a result, there is no guarantee that we can save the state of an input iterator and examine an element through that saved iterator. Input iterators, therefore, may be used only for single-pass algorithms.

### 输出迭代器

**输出迭代器（output iterator）**只能写而不能读取序列中的元素，其必须支持以下操作（其中```iter```为输出迭代器）：

- 令迭代器指向下一个元素：```++iter```；```iter++```。```++```的含义同内置的```++```的含义。
- 读取迭代器指向的元素：```*iter```。```*iter```只能出现在赋值运算符左侧。

只能向一个输出迭代器赋值一次。类似输入迭代器，输出迭代器只能用于单遍扫描算法。在泛型算法中，用作目的位置的迭代器通常都是输出迭代器。

### 前向迭代器

**前向迭代器（forward iterator）**只能在序列中沿一个方向移动，其支持所有输入迭代器与输出迭代器的操作，并可以多次读写同一个元素，因此可以保存前向迭代器的状态。使用前向迭代器的算法可对序列多遍扫描。

### 双向迭代器

**双向迭代器（bidirectional iterator）**可以在序列中正向或反向移动，其支持所有前向迭代器的操作，此外还支持以下操作：

- 令输出迭代器```iter```指向上一个元素：```--iter```；```iter--```。```--```的含义同内置的```--```的含义。

### 随机访问迭代器

**随机访问迭代器（random-access iterator）**可以在常量时间内访问序列中任意元素，其支持所有双向迭代器的操作，此外还支持以下操作（其中```iter```、```iter1```与```iter2```为随机访问迭代器，```n```为整型）：

- 比较两个迭代器在序列中的相对位置，位置越靠前越小：```iter1 < iter2```；```iter1 <= iter2```；```iter1 > iter2```；```iter1 >= iter2```。
- 令迭代器向前/向后移动```n```个位置：```iter + n```；```iter += n```；```iter - n```；```iter -= n```。
- 计算两个迭代器的距离：```iter1 - iter2```。如果结果为正，表示```iter1```位置在后；如果结果为负，表示```iter1```在前；否则两者指向同一个元素。
- 下标运算：```iter[n]```。其等价于```*(iter + n)```。

```c++
// 随机访问迭代器完成二分搜索。
// text为有序的vector<string>，sought为目标string。
auto beg = text.begin(), end = text.end();
auto mid = text.begin() + (end - beg)/2;
while (mid != end && *mid != sought) {
    if (sought < *mid) {
        end = mid;
    } else {
        beg = mid + 1;
    }
    mid = beg + (end - beg) / 2;
}
```

指针是一种随机访问迭代器。

## 迭代器实例

### 插入迭代器

插入器（inserter）（不是插入迭代器）为容器适配器，其接受一个容器，生成一个**插入迭代器（insert iterator）**，通过该迭代器可以向容器中添加元素。其支持以下操作，其中```it```为插入迭代器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>it = t</td>
        <td>假设c为it绑定的容器，依赖于插入迭代器的种类，此操作会分别调用c.push_back(t)、c.push_front(t)或c.insert(t, p)，其中p为传递给inserter的迭代器位置。</td>
    </tr>
    <tr>
        <td>*it</td>
        <td rowspan="3">返回it，除此以外不做任何操作。</td>
    </tr>
    <tr>
        <td>++it</td>
    </tr>
    <tr>
        <td>it++</td>
    </tr>
</table>

插入器有三种类型：

- ```back_inserter```：使用```push_back```插入元素的迭代器，接受一个容器参数。
- ```front_inserter```：使用```push_front```插入元素的迭代器，接受一个容器参数。
- ```inserter```：使用```insert```插入元素的迭代器，接受一个容器参数与指向该容器的迭代器。

只有支持特定函数（```push_back```、```push_front```或```insert```）的容器才能使用特定的插入迭代器。

当调用```inserter(c, iter)```时，我们得到一个迭代器，其将元素插入到```iter```原来指向的元素前。即，如果```it```是由```inserter```生成的迭代器，则赋值语句：

```c++
*it = val;
```

等价于：

```c++
it = c.insert(it, val);
++it;  // 递增it，使其指向原来的元素，即返回原来的it。
```

对于```back_inserter```与```front_insert```，其执行流程类似。

当使用```front_inserter```时，元素总是被插入到容器的第一个元素前；即使令```inserter```指向容器第一个元素，当在此元素前插入一个新元素，```inserter```仍然指向原来元素，因此不再指向容器第一个元素。

```c++
list<int> lst = {1, 2, 3, 4};
list<int> lst2, lst3;

copy(lst.cbegin(), lst.cend(), front_inserter(lst2));  // 操作完成后lst2依次包含4、3、2、1。
copy(lst.cbegin(), lst.cend(), inserter(lst3, lst3.begin()));  // 操作完成后lst3依次包含1、2、3、4。
```

### ```iostream```迭代器

#### ```istream_iterator```

```istream_iterator```通过```>>```来读取流，其支持以下操作，其中```in```、```in1```与```in2```为```istream_iterator```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>istream_iterator&lt;T&gt; in(is)</td>
        <td>创建一个名为in的istream_iterator并可从输入流is中读取元素类型为T的值。</td>
    </tr>
    <tr>
        <td>istream_iterator&lt;T&gt; end</td>
        <td>创建一个名为end的读取元素类型为T的值的istream_iterator，为尾后迭代器。</td>
    </tr>
    <tr>
        <td>in1 == in2</td>
        <td rowspan="2">比较两个迭代器是否绑定到相同的输入流或均为尾后迭代器。in1与in2必须用于读取相同类型的值。</td>
    </tr>
    <tr>
        <td>in1 != in2</td>
    </tr>
    <tr>
        <td>*in</td>
        <td>返回从流in中读取的值。</td>
    </tr>
    <tr>
        <td>in -> mem</td>
        <td>即(*in).mem。</td>
    </tr>
    <tr>
        <td>++in</td>
        <td rowspan="2">使用元素类型所定义的>>运算符从输入流中读取下一个值。++的含义同内置的++的含义。</td>
    </tr>
    <tr>
        <td>in++</td>
    </tr>
</table>

```c++
istream_iterator<int> in_iter(cin);
istream_iterator<int> eof;
while (in_iter != eof) {
    vec.push_back(*in_iter++);
}
```

```iostream```迭代器常用于泛型算法中：

```c++
// 计算从标准输入中读取的值的和。
istream_iterator<int> in(cin), eof;
cout << accumulate(in, eof, 0) << endl;
```

```istream_iterator```允许懒惰求值。即当一个```istream_iterator```被绑定到一个流时，标准库不保证迭代器立即从流中读取数据，标准库保证当```istream_iterator```被第一次解引用前，从流中读取数据的操作完成。

#### ```ostream_iterator```

```ostream_iterator```通过```<<```运算符来向流中写入数据，其支持如下操作（其中```out```为```ostream_iterator```）：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>ostream_iterator&lt;T&gt; out(os)</td>
        <td>创建一个名为out的ostream_iterator并可将类型为T的值写到输出流os中。</td>
    </tr>
    <tr>
        <td>ostream_iterator&lt;T&gt; out(os, d)</td>
        <td>创建一个名为out的ostream_iterator并可将类型为T的值写到输出流os中，每个值后面都输出一个d，d指向一个空字符结尾的字符数组。</td>
    </tr>
    <tr>
        <td>out = val</td>
        <td>使用&lt;&lt;运算符将val写入到out所绑定的ostream中，val的类型必须与out可写的类型兼容。</td>
    </tr>
    <tr>
        <td>*out</td>
        <td rowspan="3">返回out，除此以外不做任何操作。</td>
    </tr>
    <tr>
        <td>++out</td>
    </tr>
    <tr>
        <td>out++</td>
    </tr>
</table>

```c++
// 使用ostream_iterator输出值的序列。
ostream_iterator<int> out_iter(cout, " ");
for (auto e : vec)
    *out_iter++ = e;  // 或：out_iter = e，若要保持与其他迭代器的使用方式一致或行为清晰最好使用原始版本。
cout << endl;
```

```c++
// 使用ostream_iterator打印序列元素。
copy(vec.begin(), vec.end(), out_iter);  // vec为vector。
```

```c++
// 原始书店程序示例。
int main() {
    Sales_item total;
    if (cin >> total) {
        Sales_item trans;
        while (cin >> trans) {
            if (total.isbn() == trans.isbn()) {
                total += trans;
            }
            else {
                cout << total << endl;
                total = trans;
            }
        }
        cout << total << endl;
    } else {
        cerr << "No data?!" << endl;
        return -1;
    }
    return 0;
}

// 使用iostream迭代器重写原始书店程序示例主体。
istream_iterator<Sales_item> item_iter(cin), eof;
ostream_iterator<Sales_item> out_iter(cout, "\n");
Sales_item sum = *item_iter++;
while (item_iter != eof) {
    if (item_iter->isbn() == sum.isbn())
        sum += *item_iter++;
    else {
        out_iter = sum;
        sum = *item_iter++;
    }
}
out_iter = sum;
```

### 反向迭代器

反向迭代器用于反向遍历元素。对于反向迭代器，递增与递减操作的含义颠倒过来。反向迭代器必须同时支持递增与递减操作。

除了```forward_list```，其他顺序容器都支持反向迭代器。容器的```rbegin()```、```rend()```、```crbegin()```、```crend()```成员函数返回反向迭代器。其中```rbegin()```与```crbegin()```指向容器尾元素，```rend()```与```crend()```指向容器首元素前一位置。

```c++
// 逆序打印容器中的元素。
vector<int> vec = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
for (auto r_iter = vec.crbegin(); r_iter != vec.crend(); ++r_iter)
    cout << *r_iter << endl;
```

```c++
// vec为vector
sort(vec.begin(), vec.end());  // 升序排序。
sort(vec.rbegin(), vec.rend());  // 降序排序。
```

反向迭代器的```base()```方法返回一个正向迭代器，其指向该反向迭代器的后一位置，这是为了与迭代器的左闭合区间的表示方法一致。

```c++
// 逆序遍历容器，同时按正序方式输出结果。
// 打印单词列表中的最后一个单词。
auto rcomma = find(line.crbegin(), line.crend(), ',');  // line为string，保存逗号分隔的单词列表。
cout << string(line.crbegin(), rcomma) << endl;  // 错误，逆序输出最后一个单词。
cout << string(rcomma.base(), line.cend()) << endl;  // 正确。rcomma.base()指向rcomma的后一位置，line.cend()指向line.crbegin()的后一位置，这样保证两种输出方式输出的元素范围一致。
```

# 容器

**容器（container）**是一些特定类型对象的集合。标准库定义的容器包括**顺序容器（sequential container）**与**关联容器（associative containers）**。顺序容器中的元素是按照它们在容器中的位置来顺序保存与访问的，而关联容器中的元素是通过关键字（key）来保存与访问的。

> The sequential containers let the programmer control the order in which the elements are stored and accessed. That order does not depend on the values of the elements. Instead, the order corresponds to the position at which elements are put into the container. By contrast, the ordered and unordered associative containers, which we cover in Chapter 11, store their elements based on the value of a key.

> Elements in an associative container are stored and retrieved by a key. In contrast, elements in a sequential container are stored and accessed sequentially by their position in the container.

（顺序）容器几乎可以保存任何类型元素，包括容器类型。但是某些容器对元素类型有自己的特殊要求。可以为不支持特定操作的类型定义容器，但此时只能使用那些没有特殊要求的容器操作。关联容器对可保存的元素类型有更多的限制。

```c++
// 假设noDefault没有默认构造函数。
vector<noDefault> v1(10, init);  // 正确。
vector<noDefault> v2(10);  // 错误，必须提供元素初始化器。
```

## 公共操作

### 类型别名

容器定义的类型别名如下，其中```C```为容器类型：

<table>
    <tr>
        <th>类型别名</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>C::size_type</td>
        <td>无符号整型，足够保存C容器类型最大可能的容器大小。</td>
    </tr>
    <tr>
        <td>C::difference_type</td>
        <td>有符号整型，足够保存两个迭代器之间的距离。</td>
    </tr>
    <tr>
        <td>C::value_type</td>
        <td>C的元素类型。</td>
    </tr>
    <tr>
        <td>C::reference</td>
        <td>即C::value&amp;。</td>
    </tr>
    <tr>
        <td>C::const_reference</td>
        <td>即C::const_value&amp;</td>
    </tr>
</table>

### 迭代器

容器的迭代器类型如下，其中```C```为容器类型：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>C::iterator</td>
        <td>C的迭代器类型。</td>
    </tr>
    <tr>
        <td>C::const_iterator</td>
        <td>C的可以读取元素，但不能修改元素的迭代器类型。</td>
    </tr>
     <tr>
        <td>C::reverse_iterator</td>
        <td>C的反向迭代器类型。</td>
    </tr>
     <tr>
        <td>C::reverse_const_iterator</td>
        <td>C的可以读取元素，但不能修改元素的反向迭代器类型。</td>
    </tr>
</table>

如果容器（不包括set）为常量，则只能使用```const_iterator```或```reverse_const_iterator```类型。

```vector```、```deque```、```array```与```string```支持随机访问迭代器操作，```list```支持双向迭代器操作，```forward_list```支持前向迭代器操作。

#### 迭代器范围

**迭代器范围（iterator range）**由一对迭代器表示，两个迭代器分别指向同一容器中的元素或尾元素之后的位置（one past the last element），即采用**左闭合区间（left-inclusive interval）**的表示方法。这两个迭代器通常被称为“begin”与“end”，或“first”与“last”（“last”的叫法有些误导，因为第二个迭代器不会指向范围中最后一个元素，而是最后一个元素的下一个位置）。迭代器范围标记了容器中元素的范围。

当两个迭代器满足以下条件，其才能构成迭代器范围：

- 当两个迭代器分别指向同一个容器中的一个元素或尾后位置（容器最后一个元素之后的位置）。
- 可以通过对```begin```不断递增到达```end```，即```end```不能在```begin```前。

迭代器范围的编程假定：

迭代器的左闭合区间使得其由三种方便的性质：

- 如果```begin=end```，则范围为空。
- 如果```begin != end```，则范围至少包含一个元素，且```begin```指向范围中的第一个元素。
- 可以对```begin```递增若干次，使得```begin=end```。

```c++
while (begin != end) {
    *begin = val;  // 迭代器范围性质保证范围非空且begin最初指向范围中的第一个元素。
    ++begin;  // 第begin递增，获取下一个元素；当begin == end，则范围为空，退出循环。
}
```

#### 迭代器成员

特殊的迭代器操作如下所示，其中c为容器：

<table>
    <tr>
        <th>迭代器</th>
        <th>功能</th>
    </tr>
    <tr>
    	<td>c.begin()</td>
        <td>返回指向c中第一个元素的迭代器，类型为iterator或const_iterator。</td>
    </tr>
    <tr>
    	<td>c.end()</td>
        <td>返回指向c中尾后位置的迭代器，类型为iterator或const_iterator。</td>
    </tr>
    <tr>
    	<td>c.cbegin()</td>
        <td>等价于c.begin()，但是返回类型为const_iterator。</td>
    </tr>
    <tr>
    	<td>c.cend()</td>
        <td>等价于c.end()，但是返回类型为const_iterator。</td>
    </tr>
    <tr>
    	<td>c.rbegin()</td>
        <td>返回指向c中尾元素的迭代器，类型为reverse_iterator或const_reverse_iterator。</td>
    </tr>
    <tr>
    	<td>c.rend()</td>
        <td>返回指向c中首前位置的迭代器，类型为reverse_iterator或const_reverse_iterator。</td>
    </tr>
    <tr>
    	<td>c.crbegin()</td>
        <td>等价于c.rbegin()，但是返回类型为const_reverse_iterator。</td>
    </tr>
    <tr>
    	<td>c.crend()</td>
        <td>等价于c.rend()，但是返回类型为const_reverse_iterator。</td>
    </tr>
</table>
不以```c```开头的操作都是重载的，一个为```const```成员函数，返回容器的```const_iterator```或```const_reverse_iterator```类型；一个为非```const```成员函数，返回容器的```iterator```或```reverse_iterator```类型。

```c++
// a为list<string>。
list<string>::iterator it1 = a.begin();
list<string>::const_iterator it2 = a.begin();
auto it3 = a.begin();  // 仅当a为const时，it3为const_iterator。
auto it4 = a.cbegin();  // it8为const_iterator。
```

#### 迭代器失效

向顺序容器添加元素后：

- 如果容器是```vector```或```string```，且存储空间被重新分配，则指向容器的迭代器、指针与引用都会失效；如果存储空间未重新分配，则指向插入前的元素的迭代器、指针与引用不会失效，指向插入后的元素的迭代器、指针与引用都失效。
- 如果容器是```deque```，且在其首尾之外插入元素，则指向容器的迭代器、指针与引用都会失效；如果在首尾位置插入元素，则迭代器失效，但指向已存在元素的引用与指针不会失效。
- 如果容器是```list```或```forward_list```，则指向容器的迭代器、指针与引用（包括首前迭代器与尾后迭代器）仍有效。

对顺序容器删除元素后：

- 指向被删除的元素的迭代器、引用与指针均失效。
- 如果容器是```list```或```forward_list```，则指向容器的迭代器、指针与引用（包括首前迭代器与尾后迭代器）仍有效。
- 如果容器是```deque```，则在其首尾之外删除元素，则指向容器的迭代器、指针与引用都会失效；如果在尾部删除元素，则尾后迭代器失效，但指向其他位置的迭代器、指针与引用有效；如果在首部删除元素，则迭代器、引用与指针仍有效。
- 如果容器是```vector```或```string```，则指向被删位置前的迭代器、指针与引用仍有效。注意尾后迭代器总会失效。

使用失效的迭代器、指针与引用是严重的运行时错误。

```c++
// 使用迭代器删除偶数元素，复制每个奇数元素。
vector<int> vi = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
auto iter = vi.begin();  // 不能是vi.cbegin()。
while (iter != vi.end()) {
    if (*iter % 2) {
        iter = vi.insert(iter, *iter);
        iter += 2;
    } else {
        iter = vi.erase(iter);
    }
}
```

```c++
// 迭代器失效导致严重的运行时错误。
// 处理容器中的元素并在其后添加一个新元素。
// 行为未定义，许多实现可能导致无限循环。
// 启示：如果在循环中添加/删除vector、deque或string中的元素，则不要缓存end()返回的迭代器。
auto begin = v.begin(), end = v.end();  // v为一个vector。
while (begin != end) {
    ++begin;
    begin = v.insert(begin, 42);
    ++begin;
}

// 正确的处理方式。
while (begin != v.end()) {
    ++begin;
    begin = v.insert(begin, 42);
    ++begin;
}
```

### 定义与初始化

容器定义与初始化方式如下，其中```C```为容器类型：

<table>
    <tr>
        <th>定义与初始化</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>C c</td>
        <td>如果C为array，则c中元素被默认初始化，否则c为空。</td>
    </tr>
    <tr>
        <td>C c1(c2)</td>
        <td rowspan="2">c1初始化为c2的拷贝，c1与c2必须类型（包括所有模板参数，下同）相同。</td>
    </tr>
    <tr>
        <td>C c1 = c2</td>
    </tr>
    <tr>
        <td>C c{a, b, c...}</td>
        <td rowspan="2">c初始化为初始化列表中元素的拷贝，列表中的元素的类型必须与C的元素类型相容。如果C为array，则列表中元素数目必须小于等于array的大小，任何遗漏的元素被值初始化。如果以上过程无法执行，则编译器尝试构造c。</td>
    </tr>
    <tr>
        <td>C c = {a, b, c...}</td>
    </tr>
    <tr>
        <td>C c(b, e)</td>
        <td>c初始化为迭代器b与e指定范围内的元素的拷贝，范围中的元素的类型必须与C的元素类型相容。C不能为array。</td>
    </tr>
    <tr>
        <td>C seq(n)</td>
        <td>seq包含n个值初始化的元素，此构造函数为explicit的。C必须为顺序容器，且不能为array或string。显然若元素类型没有默认构造函数，不能使用之。</td>
    </tr>
    <tr>
        <td>C seq(n, t)</td>
        <td>seq包含n个初始值为t的元素。C必须为顺序容器，且不能为array。</td>
    </tr>
</table>

```c++
int int_arr[] = {0, 1, 2, 3, 4, 5};
vector<int> ivec(begin(int_arr), end(int_arr));  // 迭代器未必指向标准库容器。
vector<int> subVec(int_arr + 1, int_arr + 3);  // 迭代器未必指向标准库容器。
```

```c++
vector<string> v1{"hi"};  // 列表初始化：v1有一个元素“hi”。
vector<string> v2("hi");  // 错误。
vector<string> v3{10};  // v3有10个默认初始化的元素。
vector<string> v4{10, "hi"};  //v4有10个值为“hi”的元素。
```


```c++
int digs[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
int cpy[10] = digs;  // 错误，内置数组不支持拷贝或赋值。

array<int, 10> digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
array<int, 10> copy = digits;  // 正确。
```

### 赋值

容器的赋值运算如下，其中c、c1与c2为容器，seq为顺序容器，且不能为array：

<table>
    <tr>
        <th>运算</th>
    	<th>含义</th>
    </tr>
    <tr>
        <td>c1 = c2</td>
        <td>将c1中的元素替换为c2中的元素的拷贝，c1与c2必须类型相同。</td>
    </tr>
    <tr>
        <td>c = {a, b, c...}</td>
        <td>将c1中的元素替换为初始化列表中的元素的拷贝，c不能为array。如果以上过程无法执行，则编译器尝试构造c。</td>
    </tr>
    <tr>
        <td>swap(c1, c2)</td>
        <td rowspan="2">（逻辑上）交换c1与c2中的元素，c1与c2必须类型相同。对于array，该操作会实际交换c1与c2中的元素，且操作后指向容器的迭代器、引用与指针指向的位置不变（值发生改变）；对于其他容器，该操作只交换两个容器的内部数据结构（常数时间操作），此时除string，操作后指向容器的迭代器、引用与指针指向的元素不变但属于另一个容器；该操作会使得string的迭代器、引用与指针失效。非成员版本的swap在泛型编程中非常重要，最好统一使用非成员版本的swap。</td>
    </tr>
    <tr>
        <td>c1.swap(c2)</td>
    </tr>
    <tr>
        <td>seq.assign(b, e)</td>
        <td>将seq中的元素替换为迭代器b与e指定范围内的元素的拷贝，b与e不能指向seq中的元素。seq不能为关联容器或array。</td>
    </tr>
    <tr>
        <td>seq.assign(il)</td>
        <td>将seq中的元素替换为初始化列表il中的元素。seq不能为关联容器或array。</td>
    </tr>
    <tr>
        <td>seq.assign(n, t)</td>
        <td>将seq中的元素替换为n个值为t的元素。seq不能为关联容器或array。</td>
    </tr>
</table>


赋值操作（不包括```swap```）会导致指向左边容器的迭代器、引用与指针失效。

```c++
list<string> names;
vector<const char*> oldstyle;
names = oldstyle;  // 错误，容器类型不匹配。
names.assign(oldstyle.cbegin(), oldstyle.cend());  // 正确，可以将const char*转换为string。
```

### 大小

容器支持的大小操作如下，其中```c```为容器，```seq```为顺序容器，且不能为```array```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.size()</td>
        <td>返回容器的大小，即包含的元素数目。c不能为forward_list。</td>
    </tr>
    <tr>
        <td>c.empty()</td>
        <td>若c.size()为0，返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>c.max_size()</td>
        <td>返回一个值，该值大于等于容器可能的最大大小。</td>
    </tr>
    <tr>
        <td>seq.resize(n)</td>
        <td>将seq的大小设置为n，若n小于seq.size()，则多余的元素被丢弃；若n大于seq.size()，则新元素被值初始化。</td>
    </tr>
    <tr>
        <td>seq.resize(n, t)</td>
        <td>将seq的大小设置为n，若n小于seq.size()，则多余的元素被丢弃；若n大于seq.size()，则新元素全部被初始化t。</td>
    </tr>
</table>
### 关系运算

所有容器均支持```==```与```!=```运算符，除无序关联容器外，所有容器均支持```>```、```>=```、```<```与```<=```运算符。关系运算符的两个运算对象必须是相同类型的容器，其返回元素的字典序比较结果，即：

- 若两容器大小相等且所有对应元素相等，则两容器相等，否则不等。
- 若两容器大小不同，但较小容器中的每个元素均等于较大容器中的对应元素，则较小容器小于较大容器。
- 否则，返回第一个对应不相等的元素的比较结果。

显然，如果容器元素未定义某个关系运算符操作，则该类型的容器不支持该关系运算符操作。

## 顺序容器

标准库定义的顺序容器包括以下类型：

<table>
    <tr>
        <th>类型</th>
    	<th>含义</th>
    </tr>
    <tr>
        <td>vector</td>
        <td>可变大小数组，支持快速的随机访问，在尾部之外插入或删除元素可能很慢。vector将元素保存在连续的内存空间中。</td>
    </tr>
    <tr>
        <td>deque</td>
        <td>双端队列，支持快速随机访问，在头尾插入或删除元素很快。</td>
    </tr>
    <tr>
        <td>list</td>
        <td>双向链表，只支持双向顺序访问（bidirectional sequential access），在任何位置插入或删除元素很快。</td>
    </tr>
    <tr>
        <td>forward_list</td>
        <td>单向链表，只支持单向顺序访问（sequential access in one direction），在任何位置插入或删除元素很快。其设计目标是达到与最好的手写的单向链表相当的性能，因此不支持size操作以减少保存或计算大小的额外开销。</td>
    </tr>
    <tr>
        <td>array</td>
        <td>固定大小数组，支持随机访问，不支持插入或删除元素。</td>
    </tr>
    <tr>
        <td>string</td>
        <td>与vector相似，但专门用于保存字符。支持快速随机访问，在尾部插入或删除元素很快。</td>
    </tr>
</table>
这些容器均位于同名头文件中。

除```string```外，所有的顺序容器类型均为模板，```vector```、```deque```、```list```与```forward_list```接受一个表示容器内元素类型的模板参数，```array```接受两个模板参数，一个表示容器内元素的类型，一个表示数组大小。

```c++
array<int, 10>::size_type i;
array<int>::size_type j;  // 错误，array需要提供数组大小。
```

以下是选择顺序容器的基本原则：

- 尽可能使用```vector```。
- 如果程序有很多小元素，且对空间开销很敏感，则避免使用```list```或```forward_list```。
- 如果程序需要随机访问元素，使用```vector```或```deque```。
- 如果程序需要在容器中间插入或删除元素，则使用```list```或```forward_list```。
- 如果程序仅需要在首尾插入或删除元素，则使用```deque```。
- 如果程序只在读取输入时需要在容器中间插入元素，随后需要随机访问元素，则：
  - 确保是否真的需要在中间插入元素。当读取输入时，可以考虑使用```vector```保存读取的元素，然后调用标准库```sort```函数对容器内元素排序。
  - 如果必须在中间插入元素，考虑在输入阶段使用```list```保存元素，随后将```list```中的元素拷贝到```vector```中。
- 如果程序既需要随机访问元素，又需要在容器中间插入元素，则需要权衡考虑```list```、```forward```与```vector```与```deque```。
- 如果不确定使用哪种容器，则在程序中只使用```vector```与```list```的公共操作：只使用迭代器而不使用下标操作并避免随机访问元素，以便在必要时灵活选择```vector```或```list```。

### 访问元素

顺序容器的访问元素操作如下，其中```c```为容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.back()</td>
        <td>返回c中尾元素的引用。若c为空，则行为未定义。</td>
    </tr>
    <tr>
        <td>c.front()</td>
        <td>返回c中首元素的应用。若c为空，则行为未定义。</td>
    </tr>
     <tr>
        <td>c[n]</td>
        <td>返回c中下标为n的元素的引用，n为无符号整数，从0计数。若n >= c.size()，则行为未定义。c为vector、deque、array或string。</td>
    </tr>
     <tr>
        <td>c.at[n]</td>
        <td>同c[n]，但若n >= c.size()，则抛出out_of_range异常。c为vector、deque、array或string。</td>
    </tr>
</table>
注意，不能使用以上操作添加元素。


```c++
if (!c.empty()) {  // c为顺序容器。
    c.front() = 42;  // 将42赋给c的首元素。
    auto &v = c.back();
    v = 2024;  // 改变了c的尾元素。
    auto v2 = c.back();
    v2 = 0;  // 未改变c的尾元素。
}
```

### 添加元素

```array```不支持添加元素操作，其他顺序容器支持的添加元素操作如下，其中```c```为容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.push_back(t)</td>
        <td rowspan="2">在c的尾部插入一个值为t或由args构造（即将args作为一组实参传递给元素类型的构造函数在容器管理的空间中直接构造元素，不会创建临时对象；push操作则会创建临时对象并压入容器中）的元素，返回void。c不能为forward_list。</td>
    </tr>
    <tr>
        <td>c.emplace_back(args)</td>
    </tr>
    <tr>
        <td>c.push_front(t)</td>
        <td rowspan="2">在c的头部插入一个值为t或由args构造的元素，返回void。c不能为vector或string。</td>
    </tr>
    <tr>
        <td>c.emplace_front(args)</td>
    </tr>
    <tr>
        <td>c.insert(p, t)</td>
        <td rowspan="2">在迭代器p指向的元素前插入一个值为t或由args构造的元素，返回指向新添加的元素的迭代器。c不能为forward_list.</td>
    </tr>
    <tr>
        <td>c.emplace(p, args)</td>
    </tr>
    <tr>
        <td>c.insert(p, n, t)</td>
        <td>在迭代器p指向的元素前插入n个值为t的元素，返回指向新添加的第一个元素的迭代器，若n为0，则返回p。c不能为forward_list。</td>
    </tr>
     <tr>
        <td>c.insert(p, b, e)</td>
        <td>在迭代器p指向的元素前插入迭代器b与e指定范围内的元素，b与e不能指向c中的元素，返回指向新添加的第一个元素的迭代器，若范围为空，则返回p。c不能为forward_list。</td>
    </tr>
     <tr>
        <td>c.insert(p, il)</td>
        <td>在迭代器p指向的元素前插入初始化列表中的元素，返回指向新添加的第一个元素的迭代器，若列表为空，则返回p。c不能为forward_list。</td>
    </tr>
</table>

```push```与```insert```操作会将对象拷贝到容器中。

在使用这些操作时，注意容器的空间分配策略，这些策略会影响操作性能。

```c++
vector<string> svec;
list<string> slist;
slist.insert(slist.begin(), "Hello");  // 等价于：slist.push_front("Hello")。
svec.insert(svec.begin(), "Hello");  // 注意这可能是一个耗时操作。
```

```c++
list<string> lst;
auto iter = lst.begin();
while (cin >> word) {
    iter = lst.insert(iter, word);  // 利用insert的返回值，在lst中不断插入首元素。
}
```

### 删除元素

```array```不支持删除元素操作，其他容器支持的删除元素操作如下，其中```c```为容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.pop_back()</td>
        <td>删除c中尾元素，返回void。若c为空，则行为未定义。c不能为forward_list。</td>
    </tr>
    <tr>
        <td>c.pop_front()</td>
        <td>删除c中首元素，返回void。若c为空，则行为未定义。c不能为vector或string。</td>
    </tr>
    <tr>
        <td>c.erase(p)</td>
        <td>删除迭代器p指向的元素，返回指向被删除元素后一位置的迭代器，若p为尾后迭代器，则行为未定义。c不能为forward_list。</td>
    </tr>
    <tr>
        <td>c.erase(b, e)</td>
        <td>删除迭代器b与e指定范围内的元素，返回指向最后一个被删除元素后一位置的迭代器，若e为尾后迭代器，则返回一个尾后迭代器。c不能为forward_list。</td>
    </tr>
    <tr>
        <td>c.clear()</td>
        <td>删除c中所有元素，返回void。</td>
    </tr>
</table>

```c++
// 删除列表中的所有奇数元素。
list<int> lst = {0, 1, 2, 3, 4, 5, 6, 7, 9};
auto it = lst.begin();
while (it != lst.end()) {
    if (*it % 2) {
        it = lst.erase(it);  // 利用erase的返回值，删除lst中的奇数元素。
    } else {
        ++it;
    }
}
```

```c++
slist.clear();  // slist为list<string>。
slist.erase(slist.begin(), slist.end());  // （写）等价的调用，只是有返回值。
```

### 容量

顺序容器支持的容量操作如下，其中```c```为```vector```、```deque```或```string```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.shrink_to_fit()</td>
        <td>请求将c.capacity()减少为与c.size()相同大小。具体实现可以忽略该请求，即不保证一定退回内存空间。</td>
    </tr>
    <tr>
        <td>c.capacity()</td>
        <td>返回在不重新分配内存空间的情况下c可以保存的元素数量。c不能为deque。</td>
    </tr>
    <tr>
        <td>c.reserve(n)</td>
        <td>为c分配至少能容纳n个元素的内存空间（可能更大）。只有当n大于c.capacity()时，该操作才会改变c的容量。调用reserve永远不会减少容器占用的内存空间。c不能为deque。</td>
    </tr>
</table>
对于```vector```，只有当添加元素后```size```与```capacity```相等，或调用```resize```或```reserve```时给定的值超过当前的容量，才有可能重新分配空间。

```c++
vector<int> ivec;
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为0，capacity可能为0（取决于具体实现）。
for (vector<int>::size_type ix = 0; ix != 24; ++ix) {
    ivec.push_back(ix);
}
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为24，capacity大于等于24，可能为32（取决于具体实现）。
ivec.reserve(50);
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为24，capacity可能为50（至少50，取决于具体实现）。
while (ivec.size() != ivec.capacity())
 ivec.push_back(0);
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为50，capacity仍为50。
ivec.push_back(42);
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为51，capacity为100（至少51，取决于具体实现）。
ivec.shrink_to_fit();
cout << "ivec: size: " << ivec.size() << " capacity: " << ivec.capacity() << endl;  // size为51，capacity仍为100（至少51，取决于具体实现）。
```

### 特殊的```forward_list```操作

由于```forward_list```为单向链表，在添加或删除某个元素时，获取其前一个元素的时间代价很高，因此不支持通常的插入与删除操作。```forward_list```支持的操作如下，其中```lst```为```forward_list```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>lst.before_begin()</td>
        <td>返回指向lst首元素前一位置的迭代器，即首前（off-the-beginning）迭代器。该迭代器不能解引用，专门用于在forward_list头部插入或删除元素。lst同时支持通常的迭代器操作（即begin与end成员函数等）。</td>
    </tr>
     <tr>
        <td>lst.cbefore_begin()</td>
        <td>同lst.before_begin()，但返回const_iterator。</td>
    </tr>
    <tr>
        <td>lst.insert_after(p, t)</td>
        <td rowspan="4">在迭代器p后的位置插入元素，返回指向最后一个插入的元素的迭代器。如果范围为空，则返回p；如果p为尾后迭代器，则行为未定义。其中t为待插入的对象，n为插入的t的数量，b与e为迭代器范围，且不能指向lst中的元素，il为花括号列表。</td>
    </tr>
    <tr>
        <td>lst.insert_after(p, n, t)</td>
    </tr>
    <tr>
        <td>lst.insert_after(p, b, e)</td>
    </tr>
    <tr>
        <td>lst.insert_after(p, il)</td>
    </tr>
    <tr>
        <td>lst.emplace_after(p, args)</td>
        <td>在迭代器p指向的元素后插入一个由args构造的元素。若p为尾后迭代器，则行为未定义。</td>
    </tr>
    <tr>
        <td>lst.erase_after(p)</td>
        <td rowspan="2">删除迭代器p指向的位置后的元素，或删除迭代器b与e指定范围内的元素（不包括b，包括e，后同），返回指向最后一个被删除元素后一位置的迭代器（可能为尾后迭代器）。若p指向lst的尾元素或为尾后迭代器，则行为未定义。
       </td>
    </tr>
    <tr>
        <td>lst.erase_after(b, e)</td>
    </tr>
</table>

```c++
// 删除列表中的所有奇数元素的forward_list实现。
forward_list<int> flst = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
auto prev = flst.before_begin();  // 指向前驱。
auto curr = flst.begin();  // 指向待处理的元素。
while (cur != flst.end()) {
    if (*cur % 2) {
        curr = flst.erase_after(prev);
    } else {
        prev = cur;
        ++curr;
    }
}
```

### 额外的```string```操作

在以下操作中，```s```为```string```。

#### 构造方法

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>string s(cp, n)</td>
        <td>n为无符号数，s为cp指向的字符数组中前n个字符的拷贝，此数组必须至少包含n个字符。</td>
    </tr>
     <tr>
        <td>string s(s2, pos2)</td>
        <td>pos2为无符号数，s为string s2从下标pos2开始的字符的拷贝。若pos2大于s2.size()，则行为未定义。</td>
    </tr>
     <tr>
        <td>string s(s2, pos2, len2)</td>
        <td>pos2与len2为无符号数，s为string s2从下标pos2开始的min(len2, s2.size() - pos2)个字符的拷贝，若pos2大于s2.size()，则行为未定义。</td>
    </tr>
</table>
#### ```string```与字符数组

一般情况下，对于```string```操作，在需要使用```string```的地方，可以用空字符结尾的字符数组（包括C风格字符串）代替。例如：可以用空字符结尾的字符数组（直接或拷贝）初始化```string```或为其赋值。

```c++
string s1("Hello World");
string s1 = "hello world";
```

反之，如果需要C风格字符串，则不能直接用```string```代替之。```string```提供了```c_str```成员函数，用于返回```string```的C风格字符串，为一个指向空字符结束的字符数组的指针，类型为```const char*```，保证我们不会改变字符数组的内容。

```c++
char *str = s;  // 错误。
const char *str = s.c_str();  // 正确。
```

```c++
string s1 = "hello";
string s2 = s1 + ", " + "world";  // 正确，将字符串字面值转换为string。
string s3 = "hello" + ", " + "world";  // 错误，不能对字符串字面值执行“+”。
```

```c_str```返回的数组未必一直有效，如果后续操作改变了```string```的值，则之前返回的数组可能失效。如果强一直使用数组内容，则必须将其拷贝一份。

#### 重载运算与IO

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is &gt;&gt; s</td>
        <td>从输入流is中读取字符串到s中，返回is。字符串以空白分隔。</td>
    </tr>
    <tr>
        <td>os &lt;&lt; s</td>
        <td>将s写到输出流os中，返回os。</td>
    </tr>
    <tr>
        <td>getline(is, s)</td>
        <td>从输入流is中读取一行数据到s，返回is。</td>
    </tr>
    <tr>
        <td>s1 + s2</td>
        <td>返回string s1与string s2拼接后的结果。</td>
    </tr>
    <tr>
        <td>s1 = s2</td>
        <td>用string s2的副本替代string s1中的字符。</td>
    </tr>
</table>

#### 获取子串

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>substr s(pos, n)</td>
        <td>返回一个string，包含从s中下标pos开始的min(n, s.size() - pos)个字符的拷贝。pos默认为0，n默认为s.size() - pos。若pos大于s.size()，则抛出out_of_range异常。</td>
    </tr>
</table>

#### 修改

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>s.insert(pos, args)</td>
        <td>在pos前插入args指定的字符串。pos为下标或迭代器。若pos为下标，则返回s的引用；若pos为迭代器，则返回指向第一个插入的字符的迭代器。</td>
    </tr>
    <tr>
        <td>s.erase(pos, len)</td>
        <td>删除从下标pos开始的len个字符。若len被省略，则删除从pos开始的所有字符。返回s的引用。</td>
    </tr>
    <tr>
        <td>s.assign(args)</td>
        <td>将s中的字符替换为args指定的字符串，返回s的引用。</td>
    </tr>
    <tr>
        <td>s.append(args)</td>
        <td>将args指定的字符串追加到s，返回s的引用。</td>
    </tr>
    <tr>
        <td>s.replace(range, args)</td>
        <td>将s中范围range内的字符串替换为args指定的字符串。range为一个下标与一个长度，或一对指向s的迭代器。返回s的引用。</td>
    </tr>
</table>

其中，```args```为如下形式之一：

<table>
    <tr>
        <th>形式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>str</td>
        <td>string str。</td>
    </tr>
    <tr>
        <td>str, pos, len</td>
        <td>string str中从下标pos开始的min(len, str.size() - pos)个字符。</td>
    </tr>
    <tr>
        <td>cp, len</td>
        <td>cp指向的数组中前min(len, strlen(cp))个字符的拷贝。</td>
    </tr>
    <tr>
        <td>cp</td>
        <td>cp指向的空字符结尾的字符数组。</td>
    </tr>
    <tr>
        <td>n, c</td>
        <td>n个字符c。</td>
    </tr>
    <tr>
        <td>b, e</td>
        <td>迭代器b与e指定范围内的字符。</td>
    </tr>
    <tr>
        <td>{a, b, c...}</td>
        <td>花括号列表</td>
    </tr>
</table>

其中```append```与```assign```函数可以使用所有的```args```形式。若```insert```函数的```pos```形参为下标，则可以使用第1、2、3、5种```args```形式；若```insert```函数的```pos```形参为迭代器，则可以使用3、4、5种```args```形式。若```replace```函数的```range```形参为一个下标与一个长度，则可以使用第1、2、3、4、5种```args```形式；若```replace```函数的```range```形参为一对指向```s```的迭代器，则可以使用第1、3、4、5、6、7种```args```形式。

#### 搜索

<table>
    <tr>
        <th>形式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>s.find(args)</td>
        <td>返回s中args第一次出现的位置（args中第一个字符的下标，下同）。</td>
    </tr>
    <tr>
        <td>s.rfind(args)</td>
        <td>返回s中args最后一次出现的位置。</td>
    </tr>
    <tr>
        <td>s.find_first_of(args)</td>
        <td>返回s中args中任意一个字符第一次出现的位置。</td>
    </tr>
    <tr>
        <td>s.find_last_of(args)</td>
        <td>返回s中args中任意一个字符最后一次出现的位置。</td>
    </tr>
    <tr>
        <td>s.find_first_not_of(args)</td>
        <td>返回s中第一个不在args中的字符。</td>
    </tr>
    <tr>
        <td>s.find_last_not_of(args)</td>
        <td>返回s中最后一个不在args中的字符。</td>
    </tr>
</table>

其中```s```为```string```，```args```为以下形式之一：

<table>
    <tr>
        <th>形式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c, pos</td>
        <td>从s中下标pos处开始查找字符c，pos默认为0。</td>
    </tr>
    <tr>
        <td>s2, pos</td>
        <td>从s中下标pos处开始查找string s2，pos默认为0。</td>
    </tr>
    <tr>
        <td>cp, pos</td>
        <td>从s中下标pos处开始查找cp指向的空字符结尾的C风格字符串，pos默认为0。</td>
    </tr>
    <tr>
        <td>cp, pos, n</td>
        <td>从s中下标pos处开始查找cp指向的数组的前n个字符，pos与n无默认值。</td>
    </tr>
</table>

以上所有搜索函数的返回类型为```string::size_type```。如果未搜索到，则返回```static```成员```string::npos```，为```const string::size_type```类型，且被初始化为```-1```，因此```string::npos```等于任何```string```最大的可能大小。

```c++
// 搜索name中子串numbers出现的所有位置。
string::size_type pos = 0;
while ((pos = name.find_first_of(numbers, pos)) != string::npos) {
    cout << "found number at index: " << pos << " element is " << name[pos] << endl;
    ++pos; // move to the next character
}
```

#### 比较

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>s.compare(args)</td>
        <td>根据字符的字典序比较string s与args。若s大于args，则返回正数；若s小于args，则返回负数；否则返回0。</td>
    </tr>
</table>
其中```args```为以下形式之一：

<table>
    <tr>
        <th>形式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>s2</td>
        <td>比较s与string s2</td>
    </tr>
    <tr>
        <td>pos1, n1, s2</td>
        <td>将s中从下标pos1开始的n1个字符与string s2进行比较。</td>
    </tr>
    <tr>
        <td>pos1, n1, s2, pos2, n2</td>
        <td>将s中从下标pos1开始的n1个字符与string s2中从下标pos2开始的n2个字符进行比较。</td>
    </tr>
    <tr>
        <td>cp</td>
        <td>比较s与cp指向的空字符结尾的字符数组。</td>
    </tr>
    <tr>
        <td>pos1, n1, cp</td>
        <td>将s中从下标pos1开始的n1个字符与cp指向的空字符结尾的字符数组进行比较。</td>
    </tr>
    <tr>
        <td>pos1, n1, cp, n2</td>
        <td>将s中从下标pos1开始的n1个字符与cp指向的地址开始的n2个字符进行比较。</td>
    </tr>
</table>

#### 数值转换

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>to_string(val)</td>
        <td>一组重载函数，返回数值val的string表示。val可以是任何算术类型。对于每个浮点类型与至少与int一样大的整型，都有相应版本的to_string；小整型会被提升。</td>
    </tr>
    <tr>
        <td>stoi(s, p, b)</td>
        <td rowspan="5">返回string s的起始子串表示的整数数值，返回值类型分别为int、long、unsigned long、long long、unsigned long long。b表示转换基数，默认为10；p为size_t指针，保存s中第一个非表示数值的下标，默认为0，即：函数不保存下标。</td>
    </tr>
    <tr>
        <td>stol(s, p, b)</td>
    </tr>
    <tr>
        <td>stoul(s, p, b)</td>
    </tr>
    <tr>
        <td>stoll(s, p, b)</td>
    </tr>
    <tr>
        <td>stoull(s, p, b)</td>
    </tr>
    <tr>
        <td>stof(s, p)</td>
        <td rowspan="3">返回s的起始子串表示的浮点数数值，返回值类型分别为float、double、long double。p为size_t指针，保存s中第一个非表示数值的下标，默认为0，即：函数不保存下标。</td>
    </tr>
    <tr>
        <td>stod(s, p)</td>
        <td></td>
    </tr>
    <tr>
        <td>stold(s, p)</td>
        <td></td>
    </tr>
</table>
如果```string```无法转换为数值，则抛出```invalid_argument```异常；如果转换产生的值无法表示，则抛出```out_of_range```异常。

> If the ```string``` can’t be converted to a number, These functions throw an ```invalid_argument``` exception (§ 5.6, p. 193). If the conversion generates a value that can’t be represented, they throw ```out_of_range```.

### 容器适配器

**适配器（adaptor）**是标准库中的通用概念，本质上适配器为一种能使得某种事物表现得像其他事物的机制。一个容器适配器接受已有的（顺序）容器类型，使其表现得像另一种不同的类型。标准库定义了三个顺序容器适配器：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>stack</td>
        <td>栈。</td>
    </tr>
    <tr>
        <td>queue</td>
        <td>队列。</td>
    </tr>
    <tr>
        <td>priority_queue</td>
        <td>优先队列，默认使用元素类型的&lt;运算符建立优先级，越大的元素优先级越高，在队列中越靠前。如果要覆盖默认设置，则可以为其提供第三个模板实参，表示比较谓词。</td>
    </tr>
</table>
其中```stack```位于```stack```头文件中，```queue```与```priority_queue```位于```queue```头文件中。

所有容器均支持的类型与操作如下，其中```A```为容器适配器类型，```a```为容器适配器：

<table>
    <tr>
        <th>类型与操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>A::size_type</td>
        <td>一种类型，可以保存A类型的最大对象的大小。</td>
    </tr>
    <tr>
        <td>A::value_type</td>
        <td>A的元素类型。</td>
    </tr>
    <tr>
        <td>A::container_type</td>
        <td>实现适配器的底层容器类型。</td>
    </tr>
    <tr>
        <td>A a</td>
        <td>创建一个名为a的空适配器。</td>
    </tr>
    <tr>
        <td>A a(c)</td>
        <td>创建一个名为a的适配器，带有容器c的一个拷贝。</td>
    </tr>
    <tr>
        <td>==、!=、&lt;、&lt;=、&gt;、&gt;=</td>
        <td>返回底层容器的比较结果</td>
    </tr>
    <tr>
        <td>a.empty()</td>
        <td>若a.size()等于0，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>a.size()</td>
        <td>返回a中的元素数量。</td>
    </tr>
    <tr>
        <td>swap(a, b)</td>
        <td rowspan="2">交换a与b的内容，a与必须类型相同（包括底层容器类型）</td>
    </tr>
     <tr>
        <td>a.swap(b)</td>
    </tr>
</table>
容器适配器实现为模板，带有两个模板参数，分别表示保存的元素类型与底层容器类型。

适配器可以使用的底层容器是有限制的。所有适配器要求容器具有添加与删除元素的功能，因此底层容器不能为```array```；所有适配器要求容器具有添加、删除与访问尾元素的功能，因此不能实现为```forward_list```。```stack```要求```push_back```、```pop_back```与```back```操作，因此底层容器可以为任何剩余容器；```queue```要求```back```、```push_back```、```front```与```pop_front```操作，因此底层容器可以为```duque```或```list```，但不能为```vector```；```priority_queue```要求```front```、```push_back```与```pop_back```操作，还要有随机访问的能力，因此底层容器可以为```vector```或```deque```，但不能为```list```。```stack```与```queue```的底层容器默认为```deque```，```priority_queue```的底层容器默认为```vector```（此时不提供第二个模板参数）。

```stack```额外支持的操作如下，其中```s```为```stack```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>s.pop()</td>
        <td>删除栈顶元素，返回void。</td>
    </tr>
    <tr>
        <td>s.push(item)</td>
        <td>创建一个新元素并压入栈顶，该元素通过拷贝或移动item而来。</td>
    </tr>
    <tr>
        <td>s.emplace(args)</td>
        <td>创建一个新元素并压入栈顶，该元素通过args构造。</td>
    </tr>
    <tr>
        <td>s.top()</td>
        <td>返回栈顶元素。</td>
    </tr>
</table>
```queue```额外支持的操作如下，其中```q```为```queue```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>q.pop()</td>
        <td>删除q的首元素，返回void。</td>
    </tr>
    <tr>
        <td>q.front()</td>
        <td>返回q的首元素。</td>
    </tr>
    <tr>
        <td>q.back()</td>
        <td>返回q的尾元素。</td>
    </tr>
    <tr>
        <td>q.push(item)</td>
        <td>在queue的尾部插入值为item的元素。</td>
    </tr>
     <tr>
        <td>q.emplace(args)</td>
        <td>在queue的尾部插入args构造的元素。</td>
    </tr>
</table>
```priority_queue```额外支持的操作如下，其中```q```为```priority_queue```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>q.pop()</td>
        <td>删除q中优先级最高的元素。</td>
    </tr>
    <tr>
        <td>q.top()</td>
        <td>返回q中优先级最高的元素。</td>
    </tr>
    <tr>
        <td>q.push(item)</td>
        <td>将值item的元素插入到q中适当位置。</td>
    </tr>
    <tr>
        <td>q.emplace(args)</td>
        <td>将args构造的元素插入到q中适当位置。</td>
    </tr>
</table>
## 关联容器

两个主要的关联容器为set与map，类型如下：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>set</td>
        <td>集合，关键字即值，关键字不可重复出现。</td>
    </tr>
    <tr>
        <td>multiset</td>
        <td>关键字可重复出现的set。关键字相同的元素在容器中相邻。</td>
    </tr>
    <tr>
        <td>unordered_set</td>
        <td>用哈希函数组织的set。</td>
    </tr>
    <tr>
        <td>unordered_multiset</td>
        <td>用哈希函数组织的multiset。</td>
    </tr>
     <tr>
        <td>map</td>
        <td>关联数组（associative array），保存关键字-值对。</td>
    </tr>
    <tr>
        <td>multimap</td>
        <td>关键字可重复出现的map。关键字相同的元素在容器中相邻。</td>
    </tr>
    <tr>
        <td>unordered_map</td>
        <td>用哈希函数组织的map。</td>
    </tr>
    <tr>
        <td>unordered_multimap</td>
        <td>用哈希函数组织的multimap。</td>
    </tr>
</table>
其中，```set```与```multiset```定义在头文件```set```中，```map```与```multimap```定义在头文件```map```中；```unordered_set```与```unordered_multiset```定义在头文件```unordered_set```中，```unordered_map```与```unordered_multimap```定义在头文件```unordered_map```中。其中非哈希函数组织的关联容器为有序容器，用哈希函数组织的关联容器为无序容器。

所有的关联容器都是模板。对于set，其模板参数为关键字类型；对于map，其模板参数分别为关键字类型与值的类型。

### 类型别名

除了容器支持的通用类型外， 关联容器还支持以下类型，其中```C```为关联容器类型：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>C::key_type</td>
        <td>C的关键字类型。</td>
    </tr>
    <tr>
        <td>C::mapped_tpye</td>
        <td>与C的关键字关联的类型，只适用于map</td>
    </tr>
    <tr>
        <td>C::value_type</td>
        <td>对于set，与key_type相同；对于map，为pair&lt;const key_type, mapped_type&gt;。当解引用关联容器的迭代器时，会得到该类型。</td>
    </tr>
</table>

```c++
auto map_it = word_count.begin();  // word_count为map<string, size_t>。
cout << map_it -> first;
cout << " " map_it -> second;
map_it -> first = "new key";  // 错误：关键字是const的。
++map_it -> second;  // 正确。
```

虽然set类型同时定义了```iterator```与```const_iterator```类型（包括```reverse```版本），但是两类类型都只允许访问set中的元素。

```c++
set<int> iset = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
set<int>::iterator set_it = iset.begin();
if (set_it != iset.end()) {
    *set_it = 42;  // 错误：set中的关键字是只读的。
    cout << *set_it << endl;
}
```

### 有序容器的关键字类型要求

默认情况下，标准库使用关键字类型的```<```运算符来比较两个关键字，可以在定义时提供额外的比较函数指针类型模板参数，并在构造函数中提供比较函数（指针）实参来自定义比较函数。

```c++
// Sales_data类未定义<运算符，因此必须自定义比较函数。
bool compareIsbn(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.isbn() < rhs.isbn();
}

multiset<Sales_data, decltype(compareIsbn)*> bookstore(compareIsbn); 
```

关键字类型的比较操作必须满足**严格弱序（strict weak ordering）**，即：

- 两个关键字不能同时小于（less than）对方。
- 如果```k1```小于```k2```且```k2```小于```k3```，则```k1```必须小于```k3```。
- 如果两个关键字都不小于对方，则两个关键字相等（equivalent）。如果```k1```等于```k2```且```k2```等于```k3```，则```k1```等于```k3```。

### 关联容器与泛型算法

通常不对关联容器使用泛型算法，因为关联容器使用泛型算法时存在两大限制：

- 不能将关联容器传递给修改或重排元素的算法，因此此时关键字是```const```的。
- 关联容器可用于只读元素的算法，但是很多这类算法需要搜索序列。由于关联容器中的元素可以通过它们的关联字快速查找到，因此对关联容器使用这类泛型算法往往效率低下。应尽可能使用关联容器特有的算法。

在实际编程中，如果要对关联容器使用泛型算法，一般将其作为源序列或目的位置。如使用```copy```算法将元素从一个关联容器拷贝到另一个序列，或使用```inserter```将一个插入迭代器绑定到关联容器，从而将关联容器作为一个目的位置来调用泛型算法。

### 访问元素

关联容器支持的下标操作如下，其中```c```只能为```map```或```unordered_map```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c[k]</td>
        <td>返回关键字为k的元素（的引用，左值）。如果k不在c中，则添加一个关键字为k的元素并对其值初始化，然后将该元素返回。因为其可能插入新元素，因此只能对非const的map或unordered_map使用该操作。</td>
    </tr>
    <tr>
        <td>c.at(k)</td>
        <td>返回关键字为k的元素（的引用，左值）。如果k不在c中，则抛出out_of_range异常。</td>
    </tr>
</table>

注意map的下标运算符返回的类型与解引用map迭代器得到的类型不同。

```c++
// 单词计数程序。
map<string, size_t> word_count;
string word;
while (cin >> word) {
    ++word_count[word];
}
for (const auto &w : word_count) {
    cout << w.first << " occurs " << w.second << ((w.second > 1) ? " times" : " time") << endl；
}
```

```c++
// 单词计数程序。
// 忽略常见单词。
// 使用set保存常见单词。
map<string, size_t> word_count;
set<string> exclude = {"The", "But", "And", "Or", "An", "A", "the", "but", "and", "or", "an", "a"};
string word;
while (cin >> word) {
    if (exclude.find(word) == exclude.end()) {
        ++word_count[word];
    }
}
```

关联容器支持的访问元素操作如下，其中```c```为关联容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.find(k)</td>
        <td>返回一个迭代器，指向第一个关键字为k的元素。若k不在容器中，则返回尾后迭代器。</td>
    </tr>
    <tr>
        <td>c.count(k)</td>
        <td>返回关键字等于k的元素的数量。对于保存不重复关键字的容器，返回值总是0或1。</td>
    </tr>
    <tr>
        <td>c.lower_bound(k)</td>
        <td>返回一个迭代器，指向第一个关键字不小于k的元素。</td>
    </tr>
    <tr>
        <td>c.upper_bound(k)</td>
        <td>返回一个迭代器，指向第一个关键字大于k的元素。</td>
    </tr>
    <tr>
        <td>c.equal_range(k)</td>
        <td>返回一个迭代器pair，表示关键字等于k的元素的范围。如果k不在c中，则pair的两个成员均等于c.end()。</td>
    </tr>
</table>

如果想检查关键字是否在map中，则可以使用```find```函数：

```c++
if (word_count.find("foobar") == word_count.end()) {  // word_count为map<string, size_t>。
    cout << "foobar is not in the map" << endl;
}
```

```c++
// 在multiset或multimap中查找给定关键字的元素的三种方法。
// 查找特定作者的所有书籍。
// authors为map<string, string>，保存作者与书名的映射。

string search_item("Alain de Botton");

// 方法1：
auto entries = authors.count(search_item);
auto iter = authors.find(search_item);
while(entries) {
    cout << iter->second << endl;
    ++iter;
    --entries;
}

// 方法2：
for (auto beg = authors.lower_bound(search_item), end = authors.upper_bound(search_item); beg != end; ++beg) {
    cout << beg->second << endl;
}

// 方法3：
for (auto pos = authors.equal_range(search_item); pos.first != pos.second; ++pos.first) {
    cout << pos.first->second << endl;
}
```

```c++
// 单词转换程序。

// 根据map_file中的规则转换input中的文本。
void word_transform(ifstream &map_file, ifstream &input) {
    auto trans_map = buildMap(map_file);
    string text;
    while (getline(input, text)) {
        istringstream stream(text);
        string word;
        bool firstword = true;
        while (stream >> word) {
            if (firstword) {
                firstword = false;
            }
            else {
                cout << " ";
            }
            cout << transform(word, trans_map);
        }
        cout << endl;
    }
}

// 建立转换规则，保存到map<string, string>中。
// map_file为单词转换文件，每行保存一个单词转换规则。
// 一个规则由两部分组成：一个可能出现在输入序列中的单词与一个用来替换它的单词或短语。两者以一个空格分隔开来。
// 注意：该操作不检查file文件的合法性，假定所有规则都是有意义的（如不考虑每行只包含一个关键字与一个空格的情况）。
map<string, string> buildMap(ifstream &map_file) {
    map<string, string> trans_map;
    string key;
    string value;
    while (map_file >> key && getline(map_file, value)) {
        if (value.size() > 1) {
            trans_map[key] = value.substr(1);
        } else {
            else throw runtime_error("no rule for " + key);
        }
    }
    return trans_map;
}

// 根据转换规则进行转换。
const string &transform(const string &s, const map<string, string> &m) {
    auto map_it = m.find(s);
    if (map_it != m.cend()) {
        return map_it->second;
    }
    else {
        return s;
    }
}
```

### 添加元素

关联容器支持的添加元素操作如下，其中```c```为关联容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.insert(v)</td>
        <td rowspan="2">在c中插入value_type类型的对象v或由args构造的value_type类型的对象。对于set或map，只有当元素的关键字不在c中时才会插入元素，且返回一个pair，其first成员指向具有该关键字的元素，second成员为指示是否插入的bool值；对于multiset或multimap，总会插入元素，且返回指向新元素的迭代器。</td>
    </tr>
     <tr>
        <td>c.emplace(args)</td>
    </tr>
     <tr>
        <td>c.insert(b, e)</td>
        <td>b与e为迭代器，表示一个value_type类型值的范围。将该范围内的元素插入到c中，返回void。</td>
    </tr>
     <tr>
        <td>c.insert(il)</td>
        <td>将花括号列表il中的元素插入c中，返回void。</td>
    </tr>
     <tr>
        <td>c.insert(p, v)</td>
        <td rowspan="2">类似c.insert(p)或c.emplace(args)，但将迭代器p作为一个提示，指出从哪里开始搜索新元素应该存储的位置。返回一个迭代器，指向具有给定关键字的元素。</td>
    </tr>
     <tr>
        <td>c.insert(p, args)</td>
    </tr>
</table>
```c++
// 向map添加元素的4种方法。
// word为string，map的元素类型必须是pair。
map<string, size_t> word_count;
word_count.insert({word, 1});
word_count.insert(make_pair(word, 1));
word_count.insert(pair<string, size_t>(word, 1));
word_count.insert(map<string, size_t>::value_type(word, 1));
```

```c++
// 使用insert重写单词计数程序。
map<string, size_t> word_count;
string word;
while (cin >> word) {
    auto ret = word_count.insert({word, 1});
    if (!ret.second) {
        ++ret.first->second;
    } 
}
```

### 删除元素

关联容器支持的删除元素操作如下，其中```c```为关联容器：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.erase(k)</td>
        <td>从c中删除关键字为k的元素，返回被删除的元素的数量，返回类型为size_type。对于保存不重复关键字的容器，返回值总是0或1。</td>
    </tr>
    <tr>
        <td>c.erase(p)</td>
        <td>从c中删除迭代器p指向的元素，p必须指向c中一个真实元素，不能等于c.end()。返回指向p之后位置的迭代器，若p指向c中尾元素，则返回c.end()。</td>
    </tr>
    <tr>
        <td>c.erase(b, e)</td>
        <td>删除迭代器b与e指定范围内的元素，返回e。</td>
    </tr>
</table>
### 无序容器

标准库定义了4个**无序关联容器（unordered associative container）**：```unordered_set```、```unordered_multiset```、```unordered_map```与```unordered_multimap```，这些容器使用哈希函数与关键字类型的```==```运算符来组织元素。

如果元素类型固有就是无序的，或性能测试发现问题可以用哈希技术解决，则应使用无序容器。

除了哈希管理操作外，无序容器提供了与有序容器相同的操作（如```find```、```insert```等）。因此通常可以用一个无序容器替换对应的有序容器，反之亦然。但是无序容器内的元素未按顺序存储，因此输出通常会与有序容器的版本不同。

```c++
// 使用无序容器重写单词计数程序。
// 唯一的区别是word_count的类型。
// 得到的计数结果相同，但单词不太可能按字典序输出。
unordered_map<string, size_t> word_count;
string word;
while (cin >> word) {
    ++word_count[word];
}
for (const auto &w : word_count) {
    cout << w.first << " occurs " << w.second << ((w.second > 1) ? " times" : " time") << endl;
}
```

#### 管理桶

无序容器在存储上组织为一组桶（bucket），每个桶保存0个或多个元素。无序容器使用哈希函数将元素映射到桶，对于相同的实参，哈希函数必须产生相同的结果，具有特定哈希值的所有元素均保存在相同的桶内。如果容器允许重复关键字，则相同关键字的元素都保存在一个桶内。为了访问一个元素，容器首先计算元素的哈希值，指出应该搜索哪个桶，然后顺序搜索桶内元素来找到目标元素。因此无序容器的性能依赖于哈希函数的质量与桶的数量和大小。

无序容器支持的桶管理操作如下，其中```C```为无序容器类型，```c```为无序容器：

<table>
    <tr>
        <th>操作（桶接口）</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.bucket_count()</td>
        <td>返回正在使用的桶的数量。</td>
    </tr>
    <tr>
        <td>c.max_bucket_count()</td>
        <td>返回容器能容纳的最多的桶的数量。</td>
    </tr>
    <tr>
        <td>c.bucket_size(n)</td>
        <td>返回第n个桶内的元素数量。</td>
    </tr>
    <tr>
        <td>c.bucket(k)</td>
        <td>返回关键字k所在的桶。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作（桶迭代）</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>C::local_iterator</td>
        <td>可以用来访问桶内元素的迭代器类型。</td>
    </tr>
    <tr>
        <td>C::const_local_iterator</td>
        <td>C::local_iterator的const版本。</td>
    </tr>
    <tr>
        <td>c.begin(n)</td>
        <td>桶n的首元素迭代器。</td>
    </tr>
    <tr>
        <td>c.end(n)</td>
        <td>桶n的尾后迭代器。</td>
    </tr>
    <tr>
        <td>c.cbegin(n)</td>
        <td>同c.begin(n)，但返回const_local_iterator。</td>
    </tr>
    <tr>
        <td>c.cend(n)</td>
        <td>同c.end(n)，但返回const_local_iterator。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作（哈希策略）</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>c.load_factor()</td>
        <td>返回每个桶的平均元素数量，返回类型为float。</td>
    </tr>
    <tr>
        <td>c.max_load_factor()</td>
        <td>c试图维护的平均桶大小（桶内元素数量）。c会在需要需要时添加新桶，以使得c.load_factor() &lt;= c.max_load_factor()。</td>
    </tr>
    <tr>
        <td>c.rehash(n)</td>
        <td>重组存储，使得c.bucket_count() &gt;= n且c.bucket_count() &gt; c.size() / c.max_load_factor()。</td>
    </tr>
    <tr>
        <td>c.reserve(n)</td>
        <td>重组存储，使得c可以保存n个元素且不用rehash。</td>
    </tr>
</table>

#### 自定义hash与比较

默认情况下，无序容器使用关键字类型的```==```运算符比较元素，并使用```hash<key_type>```类型的对象生成每个元素的哈希值。标准库为内置类型（包括指针）提供了```hash```模板，还为一些标准库类型如```string```与智能指针类型了定义```hash```，因此可以直接定义关键字类型是这些类型的无序容器。但是不能直接定义关键字类型为其他自定义类类型的无序容器，此时需要为类自定义```hash```模板版本。

也可以不自定义```hash```模板版本，而使用自定义的哈希函数或相等判断运算符（equality operator）代替```==```，然后在定义容器时提供额外的哈希函数指针类型与相等判断运算符指针类型模板实参，并在构造函数中提供桶大小、哈希函数指针与相等判断运算符指针实参。

```c++
// 自定义哈希函数。
size_t hasher(const Sales_data &sd) {
    return hash<string>()(sd.isbn());
}
// 自定义比较，代替==。
bool eqOp(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.isbn == rhs.isbn();
}
```

```c++
using SD_multiset = unordered_multiset<Sales_data, decltype(hasher)*, decltype(eqOp)*>;
SD_multiset bookstore(42, hasher, eqOp);
```

如果类已定义```==```运算符，则可只覆盖哈希函数：

```c++
unordered_set<Foo, decltype(FooHash)*> fooset(10, FooHash);  // 使用FooHash生成哈希值，类Foo必须有==运算符。
```

# 泛型算法

## 原理

**泛型算法（generic algorithm）**实现了一些经典算法的公共接口（之所以被称作“算法”的原因），并且可以用于不同类型的元素和多种容器类型（包括标准库容器与内置的数组等）（之所以被称作“泛型”的原因）。

大多数泛型算法都定义在头文件```algorithm```中，标准库在头文件```numeric```中定义了一组数值泛型算法（generic numeric algorithm）。

```c++
// 因为泛型算法提供公共接口，因此可以作用于不同的容器与不同的元素类型上。

string val = "a value";
auto result = find(lst.cbegin(), lst.cend(), val);

int ia = {27, 210, 12, 47, 109, 83};
int val = 83;
int *result = find(begin(ia), end(ia), val);
auto result = find(ia + 1, ia + 4, val);  // 在序列子范围中查找。
```

泛型算法本身不会执行容器操作，它们只会运行在迭代器上并执行迭代器操作。因此，泛型算法可能改变容器中元素的值或在容器内移动元素，但是它们永远不会直接添加或删除元素。即使泛型算法通过插入器执行底层容器操作，添加元素的操作也是由插入器完成的，算法自身不会这么做。

迭代器令算法不依赖于容器。泛型算法不要求提供的多个容器类型一致。

算法依赖于元素类型的操作（element-type operation）。例如```find```用元素类型的```==```运算符完成每个元素与给定值的比较，因此元素类型必须支持该运算。但是算法不要求元素类型完全匹配，只要求算法在元素间能够执行特定的操作。

```c++
string sum = accumulate(v.cbegin(), v.cend(), "");  // 错误，用于保存和的对象的类型const char*不支持“+”。
```

```c++
equal(roater1.cbegin(), roster1.cend(), roster2.cbegin());  // roster1可以为vector<string>，roster2可以为list<const char*>，因为两个的元素可以用==来比较。
```

所有只接受单一迭代器来表示第二个序列的算法，都假定第二个序列至少与第一个序列一样长，即第一个序列中的每个元素都有与之对应的第二个序列中的元素。尤其要注意，算法不检查写操作，要确保写入的位置空间足够大，可以保存所有待写元素。

```c++
equal(roster1.cbegin(), roster1.cend(), roster2.cbegin());  // roster2指定的目的序列至少与roster1一样长。
```

```c++
vector<int> vec;  // vec为vector<int>。
fill_n(vec.begin(), vec.size(), 0);
fill_n(vec.begin(), 10, 0);  // 错误，vec为空序列。
```

保证算法有足够元素空间来容纳输出数据的常用方法是使用插入迭代器。

```c++
vector<int> vec;  // vec为vector<int>。
auto it = back_inserter(vec);  // 插入迭代器接受容器的引用，返回与容器绑定的迭代器。
*it = 42;  // vec中有一个元素，值为42；

fill_n(back_inserter(vec), 10, 0);  // 正确，添加10个元素到vec。
```

一些算法会自己向输入范围内写入元素，这些算法最多写入与给定序列一样多的元素。

```c++
// vec为vector<int>。
// 只要输入序列有效，则写入操作安全。
fill(vec.begin(), vec.end(), 0);
fill(vec.begin(), vec.begin + vec.size() / 2, 10);
```

如果算法只读取元素，则应设置相应的迭代器为```const```迭代器。如果要使用算法返回的迭代来改变元素的值，就不能使用```const```版本的迭代器。

```c++
// 泛型算法应用实例。
// 单词序列排序并去重。
void elimDups(vector<string> &words) {
    sort(words.begin(), words.end());
    auto end_unique = unique(words.begin(), words.end());
    words.erase(end_unique, words.end());
}
```

## 泛型算法结构

### 形参模式

大多数算法具有如下4种形式之一：

- ```alg(beg, end, other args)```
- ```alg(beg, end, dest, other args)```
- ```alg(beg, end, beg2, other args)```
- ```alg(beg, end, beg2, end2, other args)```

其中```alg```为算法名，```beg```、```end```、```beg2```、```end2```与```dest```均为迭代器，```other args```为额外参数。

```beg```与```end```表示算法所操作的输入范围，几乎所有算法都接受一个输入范围，是否有其他参数依赖于要执行的操作。

```dest```指定算法可以写入的目的位置。算法假定目的位置足够容纳写入的数据。如果```dest```为直接指向容器的迭代器，则算法将输出数据写到容器中已存在的元素内。更常见的情况是，```dest```被绑定到一个插入迭代器或```ostream_iterator```，插入迭代器会将新元素添加到容器中，```ostream_iterator```会将元素写入到一个输出流，保证不管写入多少元素都不会出现问题。

接受单独的```beg2```或接受```beg2```与```end2```的算法表示第二个输入范围，这些算法通常结合两个输入范围内的元素来进行一些运算。接受单独的```beg2```的算法将```beg2```作为第二个输入范围的首位置，其未指定结束位置，算法假定从```beg2```开始的范围与```beg```与```end```所表示的范围至少一样大。

### 命名规范

接受谓词参数来代替```<```或```==```的算法，以及不接受额外实参的算法，通常都是重载函数。这类函数的一个版本使用元素类型的运算符比较元素，另一个版本接受额外的谓词参数来代替```<```或```==```。

```c++
// beg与end为迭代器，comp为谓词。
unique(beg, end);
unique(beg, end, comp);
```

接受一个元素值的算法通常有另一个不同名的版本，该版本接受一个谓词来代替元素值。接受谓词参数的算法都有附加的```_if```后缀。之所以不提供重载版本，是因为可能产生重载歧义（尽管很罕见）。

```c++
// beg与end为迭代器，val为元素值，pred为谓词。
find(beg, end, val);
find_if(beg, end, pred);
```

默认情况下，重排元素的算法将重排后的元素写回给定的输入序列中，这些算法提供另一个版本，将元素写入指定的目的位置。这些算法的名字后面附加一个```_copy```。

```c++
// beg、end与dest为迭代器。
reverse(beg, end);
reverse_copy(beg, end, dest);
```

一些算法同时提供```_copy```与```_if```版本，这些版本接受一个目的位置迭代器与一个谓词。

```c++
// v1为vector<int>，v2为空vector<int>。
remove_if(v1.begin(), v1.end(), [](int i) { return i % 2; });
remove_copy_if(v1.begin(), v1.end(), back_inserter(v2), [](int i) { return i % 2; });
```

## 种类

在下面的描述中：

- ```beg```（或```beg1```）与```end```（或```end1```）为表示元素范围的迭代器（第一个输入序列），几乎所有算法都对一个由```beg```与```end```表示的序列进行操作。
- ```beg2```表示第二个输入序列开始位置的迭代器，```end2```表示第二个序列的末尾位置。如果没有```end2```，则假定```beg2```表示的序列与```beg```与```end```表示的序列一样长。```beg```与```beg2```的类型不必匹配，但是必须保证对两个序列中的元素可以执行特定的操作或调用给定的可调用对象。
- ```dest```为表示目的位置的迭代器，对于给定的输入序列，目的序列必须保证能保存同样多的元素。
- ```unaryPred```与```binaryPred```分别表示一元谓词与二元谓词，分别接受一个与两个参数，为来自输入序列的元素，两个谓词均返回可用作条件的类型。
- ```comp```为二元谓词，满足关联容器中对关键字序的要求。
- ```unaryOp```与```binaryOp```为可调用对象，可分别使用来自输入序列的一个与两个实参来调用。

数值算法定义在头文件```numeric```中，其他算法定义在头文件```algorithm```中。

### 查找对象的算法

#### 简单查找算法

以下算法要求输入迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>find(beg, end, val)</td>
        <td>返回一个迭代器，指向第一个等于val的元素。</td>
    </tr>
    <tr>
        <td>find_if(beg, end, unaryPred)</td>
        <td>返回一个迭代器，指向第一个满足unaryPred的元素。</td>
    </tr>
    <tr>
        <td>find_if_not(beg, end, unaryPred)</td>
        <td>返回一个迭代器，指向第一个不满足unaryPred的元素。</td>
    </tr>
    <tr>
        <td>count(beg, end, val)</td>
        <td>返回val出现的次数。</td>
    </tr>
    <tr>
        <td>count(beg, end, unaryPred)</td>
        <td>返回满足unaryPred的元素的数量。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>all_of(beg, end, unaryPred)</td>
        <td>如果所有元素均满足unaryPred，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>any_of(beg, end, unaryPred)</td>
        <td>如果任一元素满足unaryPred，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>none_of(beg, end, unaryPred)</td>
        <td>如果所有元素均不满足unaryPred，则返回true，否则返回false。</td>
    </tr>
</table>

#### 查找重复值的算法

以下算法要求前向迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>adjacent_find(beg, end)</td>
        <td rowspan="2">返回指向第一对相邻重复元素的迭代器。若不存在，则返回end。</td>
    </tr>
    <tr>
        <td>adjacent_find(beg, end, binaryPred)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>search_n(beg, end, count, val)</td>
        <td rowspan="2">返回一个迭代器，从该位置起有count个连续相等元素。若不存在，则返回end。</td>
    </tr>
    <tr>
        <td>search_n(beg, end, count, val, binaryPred)</td>
    </tr>
</table>
#### 查找子序列的算法

以下算法中，```find_first_of```要求用输入迭代器表示第一个序列，用前向迭代器表示第二个序列，其余算法要求两对前向迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>search(beg1, end1, beg2, end2)</td>
        <td rowspan="2">返回第二个输入序列在第一个输入序列中第一次出现的位置。若不存在，则返回end1。</td>
    </tr>
    <tr>
        <td>search(beg1, end1, beg2, end2, binaryPred)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>find_first_of(beg1, end1, beg2, end2)</td>
        <td rowspan="2">返回第二个输入序列中任一元素在第一个输入序列中第一次出现的位置。若不存在，则返回end1。</td>
    </tr>
     <tr>
        <td>find_first_of(beg1, end1, beg2, end2, binaryPred)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>find_end(beg1, end1, beg2, end2)</td>
        <td rowspan="2">返回第二个输入序列中任一元素在第一个输入序列中最后一次出现的位置。若第二个输入序列为空或位置不存在，则返回end1。</td>
    </tr>
     <tr>
        <td>find_end(beg1, end1, beg2, end2, binaryPred)</td>
    </tr>
</table>

### 其他只读算法

以下算法要求输入迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>for_each(beg, end, unaryOp)</td>
        <td>对每个元素调用unaryOp，unaryOp如果有返回值则返回值被忽略。如果迭代器允许通过解引用运算符向向元素写值，则unaryOp可能修改元素。</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>mismatch(beg1, end1, beg2)</td>
        <td rowspan="2">返回迭代器类型的pair，表示两个序列中第一对不匹配的元素。如果所有元素均匹配，则pair的第一个迭代器为end1，第二个迭代器指向beg2中偏移量等于第一个序列长度的位置。</td>
    </tr>
    <tr>
        <td>mismatch(beg1 end1, beg2, binaryPred)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>equal(beg1, end1, beg2)</td>
        <td rowspan="2">按字典序比较两个序列，如果相等则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>equal(beg1, end1, beg2, binaryPred)</td>
    </tr>
</table>

### 二分搜索算法

以下算法要求前向迭代器，且序列中的元素必须是有序的。但是这些算法都经过了优化，因此如果提供随机访问迭代器，则执行速度会快得多。搜索算法执行对数次数的比较操作，但是如果仅提供前向迭代器，则算法需要线性次数的迭代器操作在序列的元素间移动。

如果未提供```comp```，则默认使用```<```比较元素。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>lower_bound(beg, end, val)</td>
        <td rowspan="2">返回一个迭代器，指向第一个小于等于val的元素。若不存在，则返回end。</td>
    </tr>
    <tr>
        <td>lower_bound(beg, end, val, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>upper_bound(beg, end, val)</td>
        <td rowspan="2">返回一个迭代器，指向第一个大于val的元素。若不存在，则返回end。</td>
    </tr>
    <tr>
        <td>upper_bound(beg, end, val, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>equal_range(beg, end, val)</td>
        <td rowspan="2">返回一个pair，其第一个成员为lower_bound函数返回的迭代器，第二个成员为upper_bound函数返回的迭代器。</td>
    </tr>
    <tr>
        <td>equal_range(beg, end, val, comp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>binary_search(beg, end, val)</td>
        <td rowspan="2">若序列中存在于val相等的值，则返回true，否则返回false。若x不小于y且y不小于x，则x于y相等。</td>
    </tr>
    <tr>
        <td>binary_search(beg, end, val, comp)</td>
    </tr>
</table>
### 写容器元素的算法

#### 只写不读元素的算法

这些算法要求输出迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>fill(beg, end, val)</td>
        <td rowspan="4">将元素写入到序列中。元素可以是val，或执行生成器对象Gen()生成。生成器对象为可调用对象，每次调用会生成不同的返回值。无_n版本的函数均返回void，有_n版本的函数返回一个迭代器，指向写入的最后一个位置后的位置。</td>
    </tr>
    <tr>
        <td>fill_n(dest, cnt, val)</td>
    </tr>
    <tr>
        <td>generate(beg, end, Gen)</td>
    </tr>
    <tr>
        <td>generate_n(dest, cnt, Gen)</td>
    </tr>
</table>
#### 使用输入迭代器的写算法

以下算法中，```dest```为输出迭代器，其他迭代器为输入迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>copy(beg, end, dest)</td>
        <td>将输入序列中的元素拷贝到dest指定的目的序列中。</td>
    </tr>
    <tr>
        <td>copy_if(beg, end, dest, unaryPred)</td>
        <td>将输入序列中满足unaryPred的元素拷贝到dest指定的目的序列中。</td>
    </tr>
    <tr>
        <td>copy_n(beg, n, dest)</td>
        <td>将beg起始位置的n个元素拷贝到dest指定的目的序列中，输入序列必须至少有n个元素。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>move(beg, end, dest)</td>
        <td>对输入序列中的每个元素调用std::move函数，将其移动到dest指定的目的序列中。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>transform(beg, end, dest, unaryOp)</td>
        <td>将对输入序列中的元素作unaryOp操作后的结果写到dest指定的目的序列中。</td>
    </tr>
    <tr>
        <td>transform(beg, end, beg2, dest, binaryOp)</td>
        <td>将对两个输入序列中的元素对作binaryOp操作后的结果写到dest指定的目的序列中。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>replace_copy(beg, end, dest, old_val, new_val)</td>
        <td>将每个元素拷贝到dest，将old_val替换为new_val。</td>
    </tr>
     <tr>
        <td>replace_copy_if(beg, end, dest, unaryPred, new_val)</td>
        <td>将每个元素拷贝到dest，将满足unaryPred的元素替换为new_val。</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>merge(beg1, end1, beg2, end2, dest)</td>
        <td rowspan="2">将两个序列合并后的结果写入到dest中。两个序列必须都是有序的，合并后的结果也是有序的。第一个版本使用&lt;比较元素，第二个版本使用comp比较元素。</td>
    </tr>
    <tr>
        <td>merge(beg1, end1, beg2, end2, dest, comp)</td>
    </tr>
</table>
#### 使用前向迭代器的写算法

以下算法要求前向迭代器，迭代器必须有写入元素的权限。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>iter_swap(iter1, iter2)</td>
        <td>交换迭代器iter1与迭代器iter2所表示的元素，返回void。</td>
    </tr>
    <tr>
        <td>swap_ranges(beg1, end1, beg2)</td>
        <td>交换两个序列中的元素，两个序列的范围不能重叠，返回指向第二个序列中最后一个交换元素后的位置的迭代器。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>replace(beg, end, old_val, new_val)</td>
        <td>用new_val替换序列中的每个等于（使用==运算符比较）old_val的元素。</td>
    </tr>
      <tr>
        <td>replace_if(beg, end, unaryPred, new_val)</td>
        <td>用new_val替换序列中的每个满足unaryPred的元素。</td>
    </tr>
</table>
#### 使用双向迭代器的写算法

以下算法要求双向迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>copy_backward(beg, end, dest)</td>
        <td>将输入序列中的元素拷贝到dest指定的目的序列，其中dest为目的序列的尾后迭代器。元素被逆序拷贝，且在目的序列中的顺序与在输入序列中的顺序相同。如果范围为空，则返回值为dest，否则返回值指向从*beg中拷贝的元素。</td>
    </tr>
    <tr>
        <td>move_backward(beg, end, dest)</td>
        <td>将输入序列中的元素移动到dest指定的目的序列，其中dest为目的序列的尾后迭代器。元素被逆序移动，且在目的序列中的顺序与在输入序列中的顺序相同。如果范围为空，则返回值为dest，否则返回值指向从*beg中移动的元素。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>inplace_merge(beg, mid, end</td>
        <td rowspan="2">将同一个序列的两个有序子序列合并为单一有序序列，并写入到原序列中。返回void。</td>
    </tr>
    <tr>
        <td>inplace_merge(beg, mid, end, comp)</td>
    </tr>
</table>

### 划分与排序算法

划分与排序算法提供稳定与不稳定版本，其中稳定版本以```stable_```开头。稳定版本的算法保证相等元素的相对顺序，会做更多工作，运行速度可能更慢，且会消耗更多内存资源。

#### 划分算法

以下算法要求双向迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is_partitioned(beg, end, unaryPred)</td>
        <td>如果满足unaryPred的元素均在不满足unaryPred的元素前，则返回true，否则返回false。若序列为空，则返回true。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>partition_copy(beg, end, dest1, dest2, unaryPred)</td>
        <td>将输入序列中满足unaryPred的元素拷贝到dest1指定的目的序列，不满足unaryPred的元素拷贝到dest2指定的目的序列。返回一个迭代器pair，分别为两个目的序列的尾后迭代器。输入序列不能与任一目的序列重叠。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>partition_point(beg, end, unaryPred)</td>
        <td>返回满足unaryPred的序列的尾后迭代器。输入序列必须已用unaryPred划分过，如果返回值不是end，则返回值到end指定范围内的元素必须都不满足unaryPred。</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>stable_partition(beg, end, unaryPred)</td>
        <td rowspan="2">使用unaryPred划分输入序列，满足unaryPred的元素放在序列开始，不满足的元素放在序列末尾。返回一个迭代器，指向满足unaryPred的元素序列的尾后迭代器，如果所有元素均不满足unaryPred，则返回beg。</td>
    </tr>
    <tr>
        <td>partition(beg, end, unaryPred)</td>
    </tr>
</table>

#### 排序算法

以下算法要求随机访问迭代器。

如果未提供```comp```，则默认使用```<```比较元素。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>sort(beg, end)</td>
        <td rowspan="4">序列排序。返回void。</td>
    </tr>
    <tr>
        <td>stable_sort(beg, end)</td>
    </tr>
    <tr>
        <td>sort(beg, end, comp)</td>
    </tr>
    <tr>
        <td>stable_sort(beg, end, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is_sorted(beg, end)</td>
        <td rowspan="2">若序列已排序，则返回true，否则返回false。</td>
    </tr>
     <tr>
        <td>is_sorted(beg, end, comp)</td>
    </tr>
     <tr>
        <td>is_sorted_until(beg, end)</td>
        <td rowspan="2">返回序列中最长的初始有序子序列的尾后迭代器。</td>
    </tr>
     <tr>
        <td>is_sorted_until(beg, end, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>partial_sort(beg, mid, end)</td>
        <td rowspan="2">将最小的mid - beg元素排序后防止在迭代器对(beg, mid)指定的范围内。操作完成后该范围内的元素不会比mid后的元素大。返回void。</td>
    </tr>
    <tr>
        <td>partial_sort(beg, mid, end, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>partial_sort_copy(beg, end, destBeg, destEnd)</td>
        <td rowspan="2">将输入序列中的元素排序后的结果放到迭代器对(destBeg, destEnd)指定的范围中。如果目的范围大于等于输入范围，则对整个输入序列排序并存入destBeg开始的序列中，否则只有目的范围大小的排序元素被放到目的范围中。//?返回？</td>
    </tr>
    <tr>
        <td>partial_sort_copy(beg, end, destBeg, destEnd, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>nth_element(beg, nth, end)</td>
        <td rowspan="2">令迭代器nth指向的元素恰好是序列排好序后此位置上的值，且nth之前的元素均小于等于nth指向的元素，nth之后的元素均大于等于nth指向的元素。返回void。</td>
    </tr>
    <tr>
        <td>nth_element(beg, nth, end, comp)</td>
    </tr>
</table>
### 通用重排操作

以下算法要求用输出迭代器来表示目的序列。

#### 使用前向迭代器的重排算法

以下算法要求用前向迭代器来表示输入序列。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>remove(beg, end, val)</td>
        <td rowspan="4">覆盖等于val或满足unaryPred的元素，返回指向最后一个被删除元素的尾后位置的迭代器。</td>
    </tr>
    <tr>
        <td>remove_if(beg, end, unaryPred)</td>
    </tr>
    <tr>
        <td>remove_copy(beg, end, dest, val)</td>
    </tr>
    <tr>
        <td>remove_copy_if(beg, end, dest, unaryPred)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>unique(beg, end)</td>
        <td rowspan="4">覆盖相邻的重复元素。返回指向不重复元素的尾后位置的迭代器。第1、3个版本使用==比较元素，第2、4个版本使用binaryPred比较元素。</td>
    </tr>
    <tr>
        <td>unique(beg, end, binaryPred)</td>
    </tr>
    <tr>
        <td>unique_copy(beg, end, dest)</td>
    </tr>
    <tr>
        <td>unique_copy(beg, end, dest, binaryPred)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>rotate(beg, mid, end)</td>
        <td rowspan="2">循环移动序列中的元素，使得mid成为首元素。返回指向原来在beg位置的元素的迭代器。</td>
    </tr>
    <tr>
        <td>rotate_copy(beg, mid, end, dest)</td>
    </tr>
</table>

#### 使用双向迭代器的重排算法

以下算法要求用双向迭代器来表示输入序列。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>reverse(beg, end)</td>
        <td>反转序列中的元素，返回void。</td>
    </tr>
     <tr>
        <td>reverse_copy(beg, end, dest)</td>
        <td>反转序列中的元素，返回目的序列的尾后迭代器。</td>
    </tr>
</table>

#### 使用随机访问迭代器的重排算法

以下算法要求随机访问迭代器。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>random_shuffle(beg, end)</td>
        <td rowspan="3">混洗（shuffle）序列中的元素。第二个版本接受一个可调用参数rand，其必须接受一个正整数且产生0到此值包含的区间（exclusive range //?）内的一个服从均匀分布的随机整数。shuffle的第三个参数必须满足均匀分布随机数生成器（uniform random number generator）的要求。返回void。</td>
    </tr>
     <tr>
        <td>random_shuffle(beg, end, rand)</td>
    </tr>
     <tr>
        <td>shuffle(beg, end, Uniform_rand)</td>
    </tr>
</table>

### 排列算法

以下算法要求双向迭代器，且假定序列中的元素都是唯一的，即没有两个元素的值是一样的。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>is_permutation(beg1, end1, beg2)</td>
        <td rowspan="2">如果第二个序列的某个排序与第一个序列字典序相等，则返回true，否则返回false。第一个版本使用==比较元素，第二个版本使用binaryPred比较元素。</td>
    </tr>
     <tr>
        <td>is_permutation(beg1, end1, beg2, binaryPred)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>next_permutation(beg, end)</td>
        <td rowspan="2">返回序列的下一个（循环）排列。如果序列已是最后一个排列，则返回false，否则返回true。</td>
    </tr>
    <tr>
        <td>next_permutation(beg, end, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>prev_permutation(beg, end)</td>
        <td rowspan="2">返回序列的上一个（循环）排列。如果序列已是第一个排列，则返回false，否则返回true。</td>
    </tr>
    <tr>
        <td>next_permutation(beg, end, comp)</td>
    </tr>
</table>

### 有序序列的集合算法

以下算法要求用输出迭代器表示目的序列，其他迭代器为输入迭代器，所有输入序列必须已排序。

如果未提供```comp```，则默认使用```<```比较元素。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>includes(beg, end, beg2, end2)</td>
        <td rowspan="2">如果第二个序列中的每个元素都包含在第一个序列中，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>includes(beg, end, beg2, end2, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>set_union(beg, end, beg2, end2, dest)</td>
        <td rowspan="2">创建两个输入序列的有序序列，保存在dest指定的位置。两个序列都包含的元素在输出序列中只出现一次。返回目的序列的尾后迭代器。</td>
    </tr>
    <tr>
        <td>set_union(beg, end, beg2, end2, dest, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>set_intersection(beg, end, beg2, end2, dest)</td>
        <td rowspan="2">对两个序列都包含的元素创建一个有序序列，保存在dest指定的位置。返回目的序列的尾后迭代器。</td>
    </tr>
    <tr>
        <td>set_intersection(beg, end, beg2, end2, dest, comp)</td>
    </tr>
</table>

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>set_difference(beg, end, beg2, end2, dest)</td>
        <td rowspan="2">对出现在第一个序列但未出现在第二个序列中的元素创建一个有序序列，保存在dest指定的位置。返回目的序列的尾后迭代器。</td>
    </tr>
    <tr>
        <td>set_difference(beg, end, beg2, end2, dest, comp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>set_symmetric_difference(beg, end, beg2, end2, dest)</td>
        <td rowspan="2">对只出现在一个序列中的元素创建一个有序序列，保存在dest指定的位置。返回目的序列的尾后迭代器。</td>
    </tr>
    <tr>
        <td>set_symmetric_difference(beg, end, beg2, end2, dest, comp)</td>
    </tr>
</table>
### 最小值与最大值

以下算法要求输入迭代器（如果有迭代器的话）。

如果未提供```comp```，则默认使用```<```比较元素。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>min(val1, val2)</td>
        <td rowspan="4">返回val1与val2的较小值，或返回intializer_list init_list中的最小值。两个实参的类型必须完全一致（不能有转换），实参与返回值都是const的引用。</td>
    </tr>
    <tr>
        <td>min(val1, val2, comp)</td>
    </tr>
    <tr>
        <td>min(init_list)</td>
    </tr>
    <tr>
        <td>min(init_list, comp)</td>
    </tr>
    <tr>
        <td>max(val1, val2)</td>
        <td rowspan="4">返回val1与val2的较大值，或返回或返回intializer_list init_list中的最大值。两个实参的类型必须完全一致（不能有转换），实参与返回值都是const的引用。</td>
    </tr>
    <tr>
        <td>max(val1, val2, comp)</td>
    </tr>
    <tr>
        <td>max(init_list)</td>
    </tr>
    <tr>
        <td>max(init_list, comp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>minmax(val1, val2)</td>
        <td rowspan="2">返回一个pair，其first成员为val1与val2中的较小者，second成员为val1与val2中的较大者。</td>
    </tr>
    <tr>
        <td>minmax(val1, val2, comp)</td>
    </tr>
    <tr>
        <td>minmax(init_list)</td>
        <td rowspan="2">返回一个pair，其first成员为或返回intializer_list init_list中的最小者，second成员为init_list中的最大者。</td>
    </tr>
    <tr>
        <td>min_max(init_list, comp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>min_element(beg, end)</td>
        <td rowspan="2">返回指向序列中最小元素的迭代器。</td>
    </tr>
    <tr>
        <td>min_element(beg, end, comp)</td>
    </tr>
    <tr>
        <td>max_element(beg, end)</td>
        <td rowspan="2">返回指向序列中最大元素的迭代器。</td>
    </tr>
    <tr>
        <td>max_element(beg, end, comp)</td>
    </tr>
    <tr>
        <td>minmax_element(beg, end)</td>
        <td rowspan="2">返回一个pair，其first成员为序列中的最小元素，second成员为序列中的最大元素。</td>
    </tr>
    <tr>
        <td>minmax(beg, end, comp)</td>
    </tr>
</table>

#### 字典序比较

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>lexicographical_compare(beg1, end1, beg2, end2)</td>
        <td rowspan="2">根据元素的字典序比较两个序列。如果第一个序列小于第二个序列，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>lexicographical_compare(beg1, end1, beg2, end2, comp)</td>
    </tr>
</table>

### 数值算法

以下算法要求用输出迭代器表示

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>accumulate(beg, end, init)</td>
        <td>返回输入序列中所有值的和。和的初始值由init指定，和的计算使用“+”。返回类型与init的类型相同。</td>
    </tr>
    <tr>
        <td>accumulate(beg, end, init, binaryOp)</td>
        <td>返回输入序列中所有值的和。和的初始值由init指定，和的计算使用binaryOp。返回类型与init的类型相同。</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>inner_product(beg1, end1, beg2, init)</td>
        <td>返回两个序列的内积。和的初始值由init指定，积的计算使用“*”，和的计算“+”。返回类型与init的类型相同。</td>
    </tr>
    <tr>
        <td>inner_product(beg1, end1, beg2, init, binOp1, binOp2)</td>
        <td>返回两个序列的内积。和的初始值由init指定，积的计算使用binOp1，和的计算使用binOp2。返回类型与init的类型相同。</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>partial_sum(beg, end, dest)</td>
        <td rowspan="2">将新序列写入dest，每个新元素的值等于输入序列中当前位置和之前位置上所有元素之和。和的计算使用“+”或binaryOp。</td>
    </tr>
    <tr>
        <td>partial_sum(beg, end, dest, binaryOp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>adjacent_difference(beg, end, dest)</td>
        <td rowspan="2">将新序列写入dest，每个新元素的值等于输入序列中当前位置的元素与前一个位置的元素之差（除了首元素等于*beg）。差的计算使用“-”或binaryOp。</td>
    </tr>
    <tr>
        <td>adjacent_difference(beg, end, dest, binaryOp)</td>
    </tr>
</table>
<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>iota(beg, end, val)</td>
        <td>将val（的拷贝）赋予首元素并递增val（的拷贝），将递增后的值赋予下一个元素，继续递增val。依此类推。</td>
    </tr>
</table>

### 特定容器算法

链表类型```list```与```forward_list```定义了几个成员函数形式的算法。特别地，其定义了独有的```sort```、```merger```、```remove```、```reverse```与```unique```操作。通用版本的```sort```要求随机访问迭代器，不能用于链表。其他算法的通用版本可以用于链表，但代价太高。这些算法需要交换输入序列中的元素，但对于链表，可以通过改变元素间的链接而不是真的交换它们的值而实现交换操作。因此特定的算法的性能更好。

这些成员函数版本的算法如下，其中```lst```、```lst2```为链表，且必须类型相同：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>lst.merge(lst2)</td>
        <td rowspan="2">将lst合并至lst。合并后，lst2为空。lst与lst2必须是有序的，合并后的序列仍是有序的。第一个版本使用&lt;比较元素，第二个版本使用二元谓词comp比较元素。</td>
    </tr>
    <tr>
        <td>lst.merge(lst2, comp)</td>
    </tr>
    <tr>
        <td>lst.remove(val)</td>
        <td rowspan="2">删除lst中与val相等或满足一元谓词pred的元素。</td>
    </tr>
    <tr>
        <td>lst.remove_if(pred)</td>
    </tr>
    <tr>
        <td>lst.reverse()</td>
        <td>反转lst中的元素。</td>
    </tr>
    <tr>
        <td>lst.sort()</td>
        <td rowspan="2">对lst中的元素排序。第一个版本使用&lt;比较元素，第二个版本使用二元谓词comp比较元素。</td>
    </tr>
    <tr>
        <td>lst.sort(comp)</td>
    </tr>
    <tr>
        <td>lst.unique()</td>
        <td rowspan="2">删除同一个值的连续拷贝。第一个版本使用==比较元素，第二个版本使用给定的二元谓词比较元素。</td>
    </tr>
    <tr>
        <td>lst.unique(pred)</td>
    </tr>
</table>

链表还定义了```splice```算法（对于```list```，为```splice```，对于```forward_list```，为```splice_after```），此算法是链表数据结构所特有的，其参数如下，其中```lst```与```lst2```为链表，且必须类型相同：

<table>
    <tr>
        <th>形参</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>(p, lst2)</td>
        <td>p为指向list中元素的迭代器，或指向forward_list首前位置的迭代器（对于forward_list，不能是尾后迭代器）。将lst2中的所有元素移动到list中迭代器p之前的位置或forward_list中迭代器p之后的位置。元素从lst2中删除。lst与lst2不能是同一链表。</td>
    </tr>
    <tr>
        <td>(p, lst2, p2)</td>
        <td>p为指向list中元素的迭代器，或指向forward_list首前位置的迭代器（对于forward_list，不能是尾后迭代器）；p2为指向lst2中的有效（valid）的迭代器。将p2指向的元素移动到list中迭代器p之前的位置，或将p2之后的一个元素移动到forward_list中迭代器p之后的位置。lst2可以与list或forward_list为同一链表。</td>
    </tr>
    <tr>
        <td>(p, lst2, b, e)</td>
        <td>p为指向list中元素的迭代器，或指向forward_list首前位置的迭代器（对于forward_list，不能是尾后迭代器）；b与e为迭代器，表示lst2中的有效（valid）范围。将给定范围中的元素从lst2中移动到list中迭代器p之前的位置或forward_list中迭代器p之后的位置。lst2与list或forward_list可以是相同的链表，但p不能指向给定范围中的元素 。</td>
    </tr>
</table>

多数链表特有的算法与通用算法相似但不完全相同，一个重要区别是，链表特有版本会改变底层容器。

# 动态内存

程序使用静态（static）内存来保存局部```static```对象、类```static```数据成员与任何定义在函数外的变量，使用栈（stack）内存来保存定义在函数内的非```static```对象。分配在静态内存或栈内存中的变量由编译器自动创建后销毁：栈对象仅在其定义的程序块运行时才存在，```static```对象在使用前分配，在程序结束时销毁。

除了静态内存与栈内存，每个程序还拥有一个内存池，这部分内存被称作**自由空间（free store）**或**堆（heap）**。程序用堆来存储**动态分配（dynamically allocate）**，即程序运行时分配的对象。动态对象的生命周期（lifetime）由程序来控制；对于不再需要使用的对象，程序必须显式销毁它们。

程序使用动态内存的原因有三个：

- 程序不知道需要使用多少对象。容器类是典型例子。
- 程序不知道所需对象的准确类型。当使用容器存放继承体系中的对象时，可能使用动态内存。
- 程序需要在多个对象间共享数据。如下面的```StrBlob```类。

## 动态对象

### 直接管理内存

动态内存的管理通过一对运算符来完成：```new```与```delete```。

```new```后面跟随类型名，用于在动态内存中分配一个该类型的对象并返回指向该对象的指针。在分配内存时，如果要初始化对象，可以使用直接初始化的方式来初始化动态分配的对象：可以使用圆括号或列表初始化的方式初始化动态分配的对象。

默认情况下，动态分配的对象被默认初始化。

```c++
int *pi = new int;  // 未初始化，值未定义。
string *ps = new string;  // 初始化为空string。

// 显式初始化。
int *pi = new int(1024);
string *ps = new string(10, '9');
vector<int> *pv = new vector<int>{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
```

也可以直接在类型名后加上空括号对分配的对象值初始化。

```c++
string *ps = new string();
int *pi = new int();
```

```delete```接受一个动态对象的指针，销毁该指针并释放与之关联的内存。

如果一个类定义了自己的构造函数，则要求其值初始化不会产生任何影响，因此此时对象总会采用默认构造函数来初始化。

如果采用圆括号直接初始化想要分配的对象，且括号中只有一个初始值，则可以使用```auto```来自动推断想要分配的对象的类型；如果使用花括号，或括号中有多个初始值，则不能使用```auto```（因为无法推断）。

```c++
auto p1 = new auto(obj);  // p指向一个与obj类型相同的对象，该对象用obj初始化。
auto p2 = new auto{a, b, c};  // 错误。必须使用圆括号。
```

可以用```new```动态分配```const```对象，一个动态分配的```const```对象必须进行初始化。如果一个类定义了默认构造函数，则其```const```对象可以隐式初始化。由于分配的对象是```const```的，此时返回一个指向```const```的指针。

```c++
const int *pci = new const int(1024);
const string *pcs = new const string;
```

在动态分配内存中，一旦自由空间的内存被耗尽，```new```就会失败，此时```new```不能分配所要求的空间，并抛出类型为```bad_alloc```异常。可以使用```nothrow```**定位new（placement new）**阻止其抛出异常，此时如果```new```不能分配所需内存，则返回一个空指针。

```c++
int *p1 = new int;  // 如果分配失败，则抛出bad_alloc异常。
int *p2 = new (nothrow) int;  // 如果分配失败，则返回空指针。
```

```delete```后面跟随动态对象指针，负责销毁该对象，并释放与之关联的内存。```delete```后面也可跟随空指针，```delete```一个空指针总是没有问题的。释放一块非```new```分配的内存，或将相同的动态对象指针（不包括空指针）```delete```多次，其行为是未定义的。

```c++
int i, *pi1 = &i, *pi2 = nullptr;
double *pd = new double(33), *pd2 = pd;
delete i;  // 错误，i不是指针。
delete pi1;  // 未定义：pi1指向局部变量。
delete pd;  // 正确。
delete pd2;  // 未定义：pd2指向的内存已被释放。
delete pi2;  // 正确，可以delete空指针。
```

动态分配的```const```对象也可以按照相同的方式被销毁。

通过```new```动态分配的对象的生命周期一直延续到其被显式释放之前，调用者必须记得释放内存。内置类型的对象被销毁时什么也不会发生，特别地 ，当指针离开其作用域时，它所指向的对象什么也不会发生，如果指针指向的是动态内存，则内存不会被自动释放。

```c++
Foo *factory(T arg) {
    // 处理arg。
    return new Foo(arg);  // 调用者负责释放此内存。
}

// 错误使用factory函数。
void use_factory(T arg) {
    Foo *p = factory(arg);
    // 使用p但是没有delete p。
}  // p离开其作用域，但是所指向的内存没有被释放。此时没有办法再释放这块内存了。

// 正确使用factory函数。
void use_factory(T arg) {
    Foo *p = factor(arg);
    // 使用p。
    delete p;
}

// 另一种正确使用的方法：返回指针，让其他代码使用之并负责释放内存。
Foo *use_factory(T arg) {
    Foo *p = factory(arg);
    // 使用p。
    return p;
}
```

当```delete```一个指针后，指针就变成无效的了，但是在很多机器上指针仍然指向（已经释放了的）动态内存的地址，这样指针就变成了**空悬指针（dangling pointer）**，即指向一块曾经保存数据对象但现在已经无效的内存的指针。

空悬指针有未初始化指针的所有缺点，为了避免空悬指针问题，可以在指针即将离开其作用域前释放掉它所关联的内存，这样就没有机会继续使用该指针了。如果要保留指针，则可以在```delete```后将指针赋值```nullptr```，以清楚地指出指针不指向任何对象。但是如果有多个指针指向相同的内存，重置一个指向该内存的指针对其他任何任何仍指向已释放内存的指针是没有作用的。

```c++
int *p(new int (42));
auto q = p;
delete p;
p = nullptr;  // p不再指向任何对象，但是q仍然指向已经被释放的内存。
```

使用```new```与```delete```管理动态内存存在三个常见问题：

- 忘记```delete```内存，导致内存泄漏。
- 使用已经被```delete```的对象。通过在```delete```指针后将指针置为空，有时可以检测出这种错误。
- 同一块内存被```delete```两次，这种错误发生在两个指针指向相同的动态分配的对象时。

### 智能指针

为了更容易、更安全地使用动态内存，标准库提供了两种**智能指针（smart pointer）**：```shared_ptr```与```unique_ptr```，来管理动态对象。智能指针行为与类似于常规指针，但是其负责自动释放所指向的对象。标准库还定义了一个名为```weak_ptr```的伴随类，它是弱引用，指向```shared_ptr```所管理的对象。这三种类型均定义在头文件```memory```中。

```shared_ptr```与```unique_ptr```支持的操作如下，其中```p```为```shared_ptr```或```unique_ptr```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>p</td>
        <td>若p指向一个对象，则返回true。</td>
    </tr>
    <tr>
        <td>*p</td>
        <td>返回p指向的对象。</td>
    </tr>
     <tr>
        <td>p -> mem</td>
        <td>等价于(*p).mem。</td>
    </tr>
    <tr>
        <td>p.get()</td>
        <td>返回p保存的（内置）指针。需要小心使用：如果智能指针释放其指向的对象，则返回的指针指向的对象也消失了。</td>
    </tr>
    <tr>
        <td>swap(p, q)</td>
        <td rowspan="2">交换p和q中的指针。</td>
    </tr>
    <tr>
        <td>p.swap(q)</td>
    </tr>
</table>

#### ```shared_ptr```

```shared_ptr```特有的操作如下，其中```p```为```shared_ptr```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>shared_ptr&lt;T&gt; p</T></td>
        <td>定义一个可以指向类型为T的对象的空shared_ptr。</td>
    </tr>
    <tr>
        <td>make_shared&lt;T&gt;(args)</td>
        <td>返回一个shared_ptr，指向一个类型为T的动态对象，该对象使用args构造。</td>
    </tr>
     <tr>
        <td>shared_ptr&lt;T&gt;p(q)</td>
        <td>p为shared_ptr q的拷贝，q保存的指针必须可以转换为T*。此操作递增q中的计数器。</td>
    </tr>
    <tr>
        <td>p = q</td>
        <td>p与q都是shared_ptr，且所保存的指针能互相转换。此操作递减p中的计数器且递增q中的计数器。若p的引用计数变为0，则释放p管理的内存。</td>
    </tr>
    <tr>
        <td>p.unique()</td>
        <td>若p.use_count()为1则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>p.use_count()</td>
        <td>返回与p共享对象的智能指针数量；可能很慢，主要用于调试。</td>
    </tr>
</table>
当对```shared_ptr```对象拷贝或赋值时，其会记录有多少个其他```shared_ptr```执行相同的对象。可以认为每个```shared_ptr```逻辑上都有一个关联的计数器（取决于具体实现，关键在于其能记录有多少个```shared_ptr```指向相同的对象，并能在恰当时候自动释放对象），通常称为**引用计数（reference count）**，来记录这一信息。只要```shared_ptr```被拷贝，它的计数器就递增。例如：当用一个```shared_ptr```初始化其他```shared_ptr```、将其作为赋值的右侧运算对象、将其值传递给函数或从函数返回其值（而非引用），```shared_ptr```关联的计数器递增；当为```shared_ptr```赋予新值或其被销毁（例如局部的```shared_ptr```离开其作用域），则计数器递减。

> Whenever we copy a ```shared_ptr```, the count is incremented. For example, the counter associated with a ```shared_ptr``` is incremented when we use it to initialize another ```shared_ptr```, when we use it as the right-hand operand of an assignment, or when we pass it to (§ 6.2.1, p. 209) or return it from a function by value (§ 6.3.2, p. 224). The counter is decremented when we assign a new value to the ```shared_ptr``` and when the ```shared_ptr``` itself is destroyed, such as when a local shared_ptr goes out of scope (§ 6.1.1, p. 204).

```c++
auto p = make_shared<int>(42);  // p
auto q(p);  // p与q指向相同的对象，此对象有两个引用者。
```

当```shared_ptr```的计数器变为0，它会自动释放其所管理的对象。

```c++
auto r = make_shared<int>(42);  // r指向的int只有一个引用者。
r = q;  // 递增q指向的对象的引用计数，递减r原来指向的对象的引用计数。r原来指向的对象已没有引用者，被自动释放。
```

```shared_ptr```通过析构函数来递减其指向的对象的引用计数，如果引用计数变为0，则```shared_ptr```的析构函数就会销毁对象，并释放其所占用的内存。

```c++
shared_ptr<Foo> factory(T arg) {
    // 处理arg。
    return make_shared<Foo>(arg);  // shared_ptr负责delete此内存。
}

void use_factory(T arg) {
    shared_ptr<Foo> p = factory(arg);
    // 使用p。
}  // p离开其作用域，它指向的内存被自动释放。

// 另一种使用方法。
void use_factory(T arg) {
    shared_ptr<Foo> p = factory(arg);
    // 使用p。
    return p;  // 当p被返回时，引用计数递增。
}  // p离开其作用域，但它指向的内存不会被释放。
// 注意使得shared_ptr无用之后不再保留。
// 如果shared_ptr存放在一个元素中，而后不再需要全部元素，则需要使用erase删除不再需要的元素。
```

```c++
// 程序需要在多个对象间共享数据的例子。
// StrBlob类为容器类，但是StrBlob对象的不同拷贝之间可以共享相同的元素，此时如果某个对象被销毁，不能单面方地销毁底层数据。StrBlob分配的资源具有与原对象相独立的生命周期（lifetime）。StrBlob对象借助shared_ptr实现这一功能。
// StrBlob类使用默认版本的操作来拷贝、赋值与销毁该类的对象，这些操作默认拷贝、赋值与销毁对象的data成员，从而实现了预期功能。

class StrBlob {
public:
    typedef vector<string>::size_type size_type;
    StrBlob();
    StrBlob(initializer_list<string> il);
    size_type size() const {
        return data->size();
    }
    bool empty() const {
        return data->empty();
    }
    void push_back(const string &t) {
        data -> push_back(t);
    }
    void pop_back();
    string &front();
    string &back();
private:
    shared_ptr<vector<string>> data;
    void check(size_type i, const string &msg) const;
};

StrBlob::StrBlob(): data(make_shared<vector<string>>()) {}
StrBlob::StrBlob(initializer_list<string> il): data(make_shared<vector<string>>(il)) {}

void StrBlob::check(size_type i, const string &msg) const {
    if (i >= data->size()) throw out_of_range(msg);
}

string &StrBlob::front() {
    check(0, "front on empty StrBlob");
    return data->front();
}
const string &StrBlob::front() const {
    check(0, "front on empty StrBlob");
    return data->front();
}
string &StrBlob::back() {
    check(0, "back on empty StrBlob");
    return data->back();
}
const string &StrBlob::back() const {
    check(0, "back on empty StrBlob");
    return data->back();
}
void StrBlob::pop_back() {
    check(0, "pop_back on empty StrBlob");
    data->pop_back();
}
```

#### ```shared_ptr```与```new```结合使用

```shared_ptr```额外支持的操作如下，其中```p```为```shared_ptr```，```q```为内置指针，```u```为```unique_ptr```。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>shared_ptr&lt;T&gt; p(q)</td>
        <td>p管理q指向的对象，q必须指向new分配的内存，且能够转换为T*。</td>
    </tr>
    <tr>
        <td>shared_ptr&lt;T&gt; p(u)</td>
        <td>p接管了u指向的对象的所有权，将u置为空。</td>
    </tr>
    <tr>
        <td>shared_ptr&lt;T&gt; p(q, d)</td>
        <td>p接管了q指向的对象的所有权，q必须能转换为T*。p将使用可调用对象d来代替delete释放内存。</td>
    </tr>
    <tr>
        <td>shared_ptr&lt;T&gt; p (p2, d)</td>
        <td>p为shared_ptr p2的拷贝。p将使用可调用对象d来代替delete。</td>
    </tr>
    <tr>
        <td>p.reset()</td>
        <td>释放p指向的对象，令p为空。</td>
    </tr>
    <tr>
        <td>p.reset(q)</td>
        <td>令p指向q。</td>
    </tr>
    <tr>
        <td>p.reset(q, d)</td>
        <td>令p指向q，并使用可调用对象d而不是delete来释放q。</td>
    </tr>
</table>
接受指针参数的智能指针构造函数是```explicit```的。智能指针与内置指针不存在隐式转换关系。

默认情况下，一个用来初始化智能指针的普通指针必须指向动态内存，因为智能指针默认使用```delete```来释放其所关联的对象。如果要将智能指针绑定到其他类型的资源上，则必须提供自定义操作来代替```delete```。

```shared_ptr```可以协调对象的析构，但这仅限于与其自身```shared_ptr```的拷贝之间。这也是推荐使用```make_shared```而不是```new```的原因。这样就能在分配对象的同时将其与```shared_ptr```绑定，从而避免无意中将一块内存绑定到多个独立创建的```shared_ptr```上。

```c++
void process(shared_ptr<int> ptr) {
    // 使用ptr。
}  // ptr离开作用域，被销毁。

// 正确使用process的示例：
shared_ptr<int> p(new int(42));  // 引用计数为1。
process(p);  // 拷贝p会增加它的引用计数；process中引用计数为2。
int i = *p;  // 正确：引用计数为1。

// 错误使用process的示例：
int *x(new int(1024));
process(x);  // 错误，不能将int*转换为shared_ptr<int>。
process(shared_ptr<int>(x));  // 合法，但内存会被delete。
int j = *x;  // 未定义：x为悬垂指针。
```

```get```用来将指针的访问权限传递给代码，只有在确定不会```delete```该指针的情况下才能使用```get```。特别地，不要使用```get```初始化另一个智能指针或为智能指针赋值。

```c++
shared_ptr<int> p(new int(42));  // 引用计数为1。
int *q = p.get();  // 正确，但使用时注意不要让其给管理的指针被释放。
{  // 新程序块。
    shared_ptr<int>(q);  // 未定义：两个独立的shared_ptr指向相同的内存。
}  // 程序块结束，q被销毁，它指向的内存被释放。
int foo = *p;  // 未定义：p指向的内存已被释放。
```

#### ```unique_ptr```

一个```unique_ptr```“拥有”其所指向的对象，某个时刻只能有一个```unique_ptr```指向一个给定对象，当```unique_ptr```被销毁，其所指向的对象也被销毁。

```unique_ptr```特有的操作如下，其中```u```为```unique_ptr```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>unique_ptr&lt;T&gt; u1</td>
        <td rowspan="3">空unique_ptr，可以指向类型为T的对象。u1使用delete来释放它的指针，u2会使用类型为D的可调用对象来释放它的指针，u3使用类型为D的对象d来代替释放它的指针。</td>
    </tr>
    <tr>
        <td>unique_ptr&lt;T, D&gt; u2</T,></td>
    </tr>
    <tr>
        <td>unique_ptr&lt;T, D&gt; u3(d)</td>
    </tr>
    <tr>
        <td>u = nullptr</td>
        <td>释放u指向的对象，将u置空。</td>
    </tr>
    <tr>
        <td>u.release()</td>
        <td>u放弃对指针的控制权，返回指针，将u置空。</td>
    </tr>
    <tr>
        <td>u.reset()</td>
        <td rowspan="3">释放u指向的对象。如果提供了内置指针q，则令u指向这个对象，否则将u置空。</td>
    </tr>
    <tr>
        <td>u.reset(q)</td>
    </tr>
    <tr>
        <td>u.reset(nullptr)</td>
    </tr>
</table>
当定义一个```unique_ptr```时，需要将其绑定到一个```new```返回的指针上。类似```shared_ptr```，```unique_ptr```也只能采用直接初始化形式。

由于一个```unique_ptr```拥有其所指向的对象，因此```unique_ptr```不支持普通的拷贝或赋值操作。但是可以通过调用```release```或```reset```将进行指针所有权的转移。

一个例外情况是，可以拷贝或赋值一个将要被销毁的```unique_ptr```。

```c++
unique_ptr<int> clone(int p) {
    return unique_ptr<int>(new int(p));
}
```

```c++
unique_ptr<int> clone(int p) {
    unique_ptr<int> ret(new int(p));
    // ...
    return ret;
}
```

#### ```weak_ptr```

```weak_ptr```为一种不控制所指对象生存期的智能指针，指向一个由```shared_ptr```管理的对象。将一个```weak_ptr```绑定到一个```shared_ptr```不会改变```shared_ptr```的引用计数，一旦最后一个```shared_ptr```被销毁，则绑定的对象被释放。

```weak_ptr```支持的操作如下，其中```w```为```weak_ptr```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>weak_ptr&lt;T&gt; w</td>
        <td>空weak_ptr，可以指向类型为T的对象。</td>
    </tr>
    <tr>
        <td>weak_ptr&lt;T&gt; (sp)</td>
        <td>与shared_ptr sp指向相同对象的weak_ptr。T必须能转换为sp指向的类型。</td>
    </tr>
    <tr>
        <td>w = p</td>
        <td>p为一个shared_ptr或unique_ptr，赋值后w与p共享对象。</td>
    </tr>
    <tr>
        <td>w.reset()</td>
        <td>置空w。</td>
    </tr>
    <tr>
        <td>w.use_count()</td>
        <td>返回与w共享对象的shared_ptr的数量。</td>
    </tr>
    <tr>
        <td>w.expired()</td>
        <td>若w.use_count()为0，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>w.lock()</td>
        <td>如果expired为true，则返回一个空shared_ptr，否则返回一个指向w的对象的shared_ptr。</td>
    </tr>
</table>

当创建一个```weak_ptr```时，要用一个```shared_ptr```来初始化它。由于对象可能不存在，因此不能使用```weak_ptr```直接访问对象，而必须调用```lock```函数。

```c++
auto p = make_shared<int>(42);
weak_ptr<int> wp(p);
if (shared_ptr<int> np = wp.lock()) {
    // 此时wp与p共享对象。
}
```

```c++
// StrBlob的一个伴随类。

class StrBlobPtr {
public:
    StrBlobPtr(): curr(0) {}
    StrBlobPtr(StrBlob &a, size_t sz = 0): wptr(a.data), curr(sz) {}
    string &deref() const;
    StrBlobPtr &incr();
private:
    shared_ptr<vector<string>> check(size_t, const string&) const;
    weak_ptr<vector<string>> wptr;
    size_t curr;
};

shared_ptr<vector<string>> StrBlobPtr::check(size_t i, const string &msg) const {
    auto ret = wptr.lock();
    if (!ret) {  // 阻止用户访问一个不存在的数组。
        throw runtime_error("unbound StrBlobPtr");
    }
    if (i >= ret->size()) {  // 阻止用户访问越界索引。
        throw out_of_range(msg);
    }
    return ret;
}

string &StrBlobPtr::deref() const {
    auto p = check(curr, "dereference past end");
    return (*p)[curr];
}

StrBlobPtr &StrBlobPtr::incr() {
    check(curr, "increment past end of StrBlobPtr");
    ++curr;
    return *this;
}

// 前向声明StrBlobPtr并为StrBlob新增成员函数，无const版本。
// StrBlobPtr的定义随后。
class StrBlobPtr;
class StrBlob {
    friend class StrBlobPtr;
    StrBlobPtr begin() {
        return StrBlobPtr(*this);
    }
    StrBlobPtr end() {
        auto ret = StrBlobPtr(*this, data->size());
        return ret;
    }
};
```

#### 智能指针与哑类（dumb class）

不是所有的类都定义了合适的析构函数来清理对象使用的资源，与管理动态内存类似，这些类也会发生资源泄露问题，也可采用类似的方法来管理。此时智能指针需要自定义**删除器（deleter）**来代替默认的```delete```。对于```shared_ptr```，自定义删除器只需要在构造函数中提供可调用对象；对于```unqiue_ptr```，则还需要提供可调用对象类型模板实参。

```c++
// 假设正在使用一个C与C++都使用的网络库：

struct destination;  // 连接目的。
struct connection;  // 连接所需信息。
connection connect(destination*);  // 打开连接。
void disconnect(connection);  // 关闭连接。

// 类似于直接管理内存。
void f(destination &d) {
    connection c = connect(&d);  // 获得一个连接，使用完成后记得关闭。
    // 使用连接。
    // 如果f退出前忘记调用disconnect，就无法关闭c了。
}


// 使用shared_ptr管理连接。
void end_connection(connection *p) {
    disconnect(*p);
}
void f(destination &d /* 其他参数 */) {
    connection c = connect(&d);
    shared_ptr<connection> p(&c, end_connection);
    // 使用连接。
    // 当f退出（即使是因为异常而退出），连接被正确关闭。
}

// 使用unique_ptr管理连接。
void f(destination &d /* 其他参数 */) {
    connection c = connect(&d);
    unique_ptr<connection, decltype(end_connection)*> p(&c, end_connection);
    // 使用连接。
    // 当f退出（即使是因为异常而退出），连接被正确关闭。
}
```

#### 智能指针与异常

如果使用智能指针，即使程序块过早结束，智能指针类也能保证内存不再需要时被释放。

```c++
void f() {
    shared_ptr<int> sp(new int(42));
    // 抛出一个未在f中捕获的异常。
}  // 函数结束时shared_ptr自动释放内存。
```

在这种情况下，直接管理的内存不会被自动释放。

```c++
void f() {
    int *ip = new int(42);
    // 抛出一个未在f中捕获的异常。
    delete ip;
}  // 函数结束，ip被销毁，没有指针指向ip原指向的内存，内存永远不会被释放。
```

## 动态数组

C++语言和标准库提供两种一次分配一个对象数组的方法。

当一个应用需要可变数量的对象时，在```StrBlob```中采用的方法（即使用标准库容器）几乎总是更简单、快速与安全。大多数容器应该使用标准库容器而不是动态分配的数组，因为其更简单、更不容易出现内存管理错误且可能有更好的性能。由于标准库支持移动操作，使用该方法的优势会更明显（速度的极大提升）。

### ```new```分配动态数组

通过在```new```表达式的类型名后跟上一对方括号并在其中指明要分配的对象的数量，来分配含有指定数量元素的动态数组，数量必须是整型但不必是常量。如果分配成功，```new```返回指向第一个对象的指针。

```c++
int *pia = new int[get_size()];  // 调用get_size函数确定分配多少个int。
```

```c++
// 使用表示数组类型的类型别名来分配一个动态数组：
typedef int arrT[42];
int *p = new arrT;  // 编译器同样使用new[]来分配动态数组。
```

当用```new```分配一个数组时，实际上得到的是一个数组元素类型的指针（连要分配的对象的数量都没有），而不是一个数组类型的对象。即使采用类型别名定义一个数组类型，```new```也不会分配一个数组类型的对象。因此不能对动态数组调用```begin```与```end```函数，也不能使用范围for语句来处理动态数组中的元素。

默认情况下，```new```分配的动态数组被默认初始化，可以值初始化数组中的元素，方法是在方括号后跟一对空括号。空括号内不能有初始值意味着不能使用```auto```分配动态数组。

```c++
int *pia = new int[10];  // 10个未初始化的int。
int *pia2 = new int[10]();  // 10个值初始化为0的int。
string *psa = new string[10];  // 10个空string。
string *psa2 = new string[10]();  // 10个空string。
```

可以提供一个花括号列表初始化动态数组，初始化规则与内置数组的列表初始化相同，特别地，如果花括号列表元素数量大于动态数组大小，则```new```表达式失败，不会分配任何内存，抛出```bad_array_new_length```（位于头文件```new```中）异常。

```c++
int *pia3 = new int[10] {0,1,2,3,4,5,6,7,8,9};
string *psa3 = new string[10] {"a", "an", "the", string(3,'x')};
```

可以动态分配一个大小为0的动态数组，这与内置数组不同。

```c++
size_t n = get_size();  // get_size函数返回需要的元素数量。
int *p = new int[n];
for (int *q = p; q != p + n; ++q) { // 若get_size函数返回0，则跳过循环体。
    /* ...
}
```

```delete []```后面跟随动态数组用于释放该动态数组，此时动态数组中的元素按逆序销毁。```delete []```后面也可跟随空指针。除此以外，不允许跟随其他类型对象。如果方括号被忽略，则行为未定义，即使使用表示数组类型的类型别名来分配一个动态数组，方括号也是必须的。

#### 智能指针管理动态数组

标准库提供了一个可以管理```new```分配的动态数组的```unique_ptr```版本，其额外支持的操作如下：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>unique_ptr&lt;T[]&gt; u</td>
        <td>u可指向一个动态分配的数组，数组的元素类型为T。</td>
    </tr>
    <tr>
        <td>unique_ptr&lt;T[]&gt; u(p)</td>
        <td>u指向内置指针p指向的动态分配的数组。p必须能转换为类型T*。</td>
    </tr>
    <tr>
        <td>u[i]</td>
        <td>返回unique_ptr u数组中的下标i处的对象。u必须指向一个数组。</td>
    </tr>
</table>

该版本的```unique_ptr```不支持成员访问运算符，其他```unique_ptr```操作不变。

该版本的```unique_ptr```默认使用```delete[]```销毁其管理的对象。

```c++
unique_ptr<int[]> up(new int[10]);
for (size_t i = 0; i != 10; ++i) {
    up[i] = i;
}
up.release();  // 自动调用delete[]。
```

如果要使用```shared_ptr```管理动态数组，则必须自定义删除器，否则其默认使用```delete```销毁其管理的动态数组（相当于释放动态数组时忘记了方括号），而这是未定义的。

```c++
shared_ptr<int> sp(new int[10], [](int *p) { delete[] p; });
```

因为```shared_ptr```不直接支持动态数组管理，所以不能使用下标运算符以及指针算术运算访问数组中的元素，而是必须使用```get```函数获取内置指针，然后用它来访问数组元素。

```c++
for (size_t i = 0; i != 10; ++i) {
    *(sp.get() + i) = i;
}
```

### ```allocator```类

```new```的局限性在于其将内存分配与对象构造组合在了一起。类似地，```delete```将对象析构与内存释放组合在了一起。当分配单个对象时，通常希望发生这种情况，此时几乎肯定知道对象应该有什么值。但是当分配一块内存时，通常需要在内存上按需构造对象，此时希望内存分配与对象构造分离。

一般情况下，将内存分配与对象构造组合在一起会导致不必要的浪费。元素可能在初始化后又被赋值；一些元素可能不会用到，但是也被构造。更重要的是，那些没有默认构造函数的类不能动态分配数组。

```c++
// 该例中，数组元素被写了两次，一次发生在默认初始化时，一次发生在循环体中。
string *const p = new string[n];
string s;
string *q = p;
while (cin >> s && q != p + n) {
    *q++ = s;
}
const size_t size = q - p;
delete[] p;
```

```allocaotr```类可以将内存分配与对象构造分类开来，其定义在头文件```memory```中，支持的操作如下，其中```a```为```allocaotr```。

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>allocator&lt;T&gt; a</td>
        <td>定义一个allocator对象，可以为类型为T的对象分配内存。</td>
    </tr>
    <tr>
        <td>a.allocate(n)</td>
        <td>分配一段原始的、为构造的内存，保存n个类型为T的对象。</td>
    </tr>
    <tr>
        <td>a.deallocate(p, n)</td>
        <td>释放从指针p中地址开始的保存n个类型为T的对象的内存，p必须是先前由allocate返回的指针，且n必须是p创建时所要求的的大小。在调用之前，用户必须对在这块内存中的每个对象调用destroy。</td>
    </tr>
    <tr>
        <td>a.construct(p, args)</td>
        <td>p必须是一个类型为T*的指针且指向一块原始内存。在p指向的内存中通过args构造一个类型为T的对象。</td>
    </tr>
    <tr>
        <td>a.destroy(p)</td>
        <td>对T *p指向的对象执行析构函数。</td>
    </tr>
</table>

```c++
allocator<string> alloc;
auto const p = alloc.allocate(n);  // 分配内存，n >= 3。
auto q = p;
// 构造对象：
alloc.construct(q++);
alloc.construct(q++, 10, 'c');
alloc.construct(q++, "hi");
// 销毁对象：
while (q != p) {
    alloc.destroy(--q);
}
```

一旦对象被销毁，就可以重新使用这部分内存来保存其他对象，也可以释放内存将其归还给系统。

```c++
alloc.deallocate(p, n);
```

使用未构造的内存的行为是未定义的；只能对已被构造的元素执行```destroy```操作。

#### 拷贝与填充未初始化内存的算法

标准库为```allocator```类定义了两个伴随算法，用于在未初始化的内存中创建对象。这些算法都定义在头文件```memory```中。这些算法都是在目标位置构造元素，而不是为它们赋值。

> These functions construct elements in the destination, rather than assigning to them.

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>uninitialized_copy(b, e, b2)</td>
        <td>将迭代器b与e指定范围内的元素拷贝到迭代器b2指定的未构造的原始内存中。b2指向的内存必须足够大，能够容纳序列中的元素。返回递增后的目的位置迭代器。</td>
    </tr>
      <tr>
        <td>uninitialized_copy_n(b, n, b2)</td>
        <td>从迭代器b指向的位置开始，拷贝n个元素到b2开始的原始内存中。返回递增后的目的位置迭代器。</td>
    </tr>
      <tr>
        <td>uninitialized_fill(b, e, t)</td>
        <td>在迭代器b与e指定的原始内存范围中创建对象，对象均为t的拷贝。返回void。</td>
    </tr>
      <tr>
        <td>uninitialized_fill_n(b, n, t)</td>
        <td>在迭代器b指向的未构造的、原始的内存中创建unsigned n个对象，对象均为t的拷贝。内存必须足够大，能够容纳给定数量的对象。返回递增后的目的位置迭代器。</td>
    </tr>
</table>

```c++
auto p = alloc.allocate(vi.size() * 2);  // vi为vector<int>。
auto q = uninitialized_copy(vi.begin(), vi.end(), p);
uninitialized_fill_n(q, vi.size(), 42);
```

```c++
/* 一个简单的文本查询程序，用于演示智能指针的使用。
 * 该程序允许用户在一个给定文件中查询单词，查询结果包括单词在文件中出现的次数及其所在行的列表，行按升序输出。如果一个单词在一行中出现多次，则此行在列表中仅出现一次。
 * 虽然可以使用标准库容器vector、set与map来直接编写文本查询程序，但是定义一个更为抽象的解决方案会更好。本例使用TextQuery来执行查询操作，使用QueryResult类存储查询的结果。两个类在交互时，可以将结果信息进行拷贝，如拷贝保存行号的set甚至是保存整个文件的vector但这种操作可能代价很大，而且通常不需要整个vector执行这种拷贝带来不必要的浪费。
 * 通过返回指向TextQuery对象内部的迭代器（或指针），可以避免这种拷贝。但是如果TextQuery对象在对应的QueryResult对象之前销毁，QueryResult就将引用一个不再存在的对象中的数据。为了保持两个类对象的生存期的同步，可以使用shared_ptr来反映两者概念上“共享”数据的关系。
 */

class QueryResult;

class TextQuery {
public:
	using line_no = vector<string>::size_type;  // QueryResult类中也可再定义line_no类型别名，以避免使用：TextQuery::line_no。
	TextQuery(ifstream&);
	QueryResult query(const string&) const;
private:
	shared_ptr<vector<string>> file;
	map<string, shared_ptr<set<line_no>>> wm;
};

TextQuery::TextQuery(ifstream &is) : file(new vector<string>) {
	string text;
	while (getline(is, text)) {
		file->push_back(text);
		int n = file->size() - 1;
		istringstream line(text);
		string word;
		while (line >> word) {
			auto &lines = wm[word];
			if (!lines) {
                lines.reset(new set<line_no>);
            }
			lines -> insert(n);
		}
	}
}

class QueryResult {
	friend ostream &print(ostream&, const QueryResult&);
public:
	QueryResult(string s, shared_ptr<set<TextQuery::line_no>> p, shared_ptr<vector<string>> f) : sought(s), lines(p), file(f) {}
private:
	string sought;
	shared_ptr<set<TextQuery::line_no>> lines;
	shared_ptr<vector<string>> file;
};

QueryResult TextQuery::query(const string &sought) const {
	static shared_ptr<set<line_no>> nodata(new set<line_no>);
	auto loc = wm.find(sought);
	if (loc == wm.end()) {
		return QueryResult(sought, nodata, file);
    }
	else {
        return QueryResult(sought, loc -> second, file);
    }
}

ostream &print(ostream &os, const QueryResult &qr) {
	os << qr.sought << " occurs " << qr.lines -> size() << " " << make_plural(qr.lines->size(), "time", "s") << endl;
	for (auto num : *qr.lines) {
        os << "\t(line " << num + 1 << ") " << *(qr.file -> begin() + num) << endl;
    }
    return os;
}

// 输出单词正确的单复数形式。
string make_plural(size_t ctr, const string &word, const string &ending) {
	return (ctr > 1) ? word + ending : word;
}

// 使用演示。
void runQueries(ifstream &infile) {
	TextQuery tq(infile);
	while (true) {
		cout << "enter word to look for, or q to quit: ";
		string s;
		if (!(cin >> s) || s == "q")
			break;
		print(cout, tq.query(s)) << endl;
	}
}
```

# 重载运算

当运算符作用于类类型的运算对象时，可以通过运算符重载（operator overloading）重新定义运算符的含义。

重载的运算符（overloaded operator）是具有特殊名字的函数：它们的名字由关键字```operator```紧跟要定义的运算符符号共同组成。和其他函数一样，重载的运算符也含有返回类型、形参列表与函数体。

重载的运算符函数的参数数量与该运算符作用的运算对象的数量一样多，且参数顺序就是运算对象的顺序。除了重载的函数调用运算符外，其他重载的运算符不能有默认实参。

如果一个运算符函数是成员函数，则其第一个运算对象被绑定到隐式的```this```指针上，因此该函数显式的参数数量比运算符的运算对象数量少一个。

运算符函数要么是类的成员，要么至少含有一个类类型的参数。因此运算符作用域内置类型的运算对象时，其含义无法被改变。

```c++
int operator+(int, int);  // 错误。
```

只能重载已有的运算符，不能发明新的运算符。

以下运算符可以被重载：```+```、```-```、```*```、```/```、```%```、```^```、```&```、```|```、```~```、```!```、```,```、```=```、```<```、```>```、```<=```、```>=```、```++```、```--```、```<<```、```>>```、```==```、```!=```、```&&```、```||```、```+=```、```-=```、```/=```、```%=```、```^=```、```&=```、```|=```、```*=```、```<<=```、```>>=```、```[]```、```()```、```->```、```->*```、```new```、```new[]```、```delete```、```delete[]```。

有四个符号（```+```，```-```，```*```与```&```）既是一元运算符又是二元运算符，这些运算符都能被重载，从形参数量可以推断出定义的是哪种运算符。

运算符重载不能改变运算符固有的优先级与结合律。

```c++
x == y + z;  // 无论运算对象类型是什么，都等价于：x == (y + z)。
```

以下4个运算符不能被重载：```::```、```.*```、```.```、```? :```。

```c++
::    .*    .    ? :
```

```&&```、```||```、```,```与```&```不应被重载。因为```&&```与```||```运算符的短路求值规则与求值顺序规则无法保留；```,```运算符的求值顺序规则无法保留；并且C++已经规定了```,```与```&```作用于类类型对象时的特殊含义，这与大多数运算符都不同，由于这两个运算符已有内置含义，如果被重载，则其行为将异于常态。

可以将运算符作用于类型正确的实参从而间接“调用”运算符函数，也可以像普通函数（包括成员函数）那样直接调用运算符函数。

```c++
data1 + data2;  // data1与data2都是类类型对象。间接调用。
operator+(data1, data2);  // 直接调用。

data1 += data2;  // 间接调用，this绑定到data1的地址。
data1.operator+=(data2);  // 直接调用，this绑定到data1的地址。
```

以下准则用于决定将运算符作为成员函数还是普通的非成员函数：

- 赋值（```=```）、下标（```[]```）、调用（```()```）与成员访问运算符（```->```）必须是成员函数。
- 复合赋值运算符通常应该被定义为成员函数（不作硬性要求）。
- 改变对象状态的运算符或与给定类型密切相关的运算符，如递增递减运算符与解引用运算符，通常应该是成员。
- 可能转换其任意运算对象的具有对称性的运算符，如算术、相等、关系与位运算符，通常应该被定义为非成员函数。如果想提供含有类对象的混合类型表达式（其中一个类型可以转换为另一个类型，并期望运算对象可以是任意顺序），则运算符必须被定义成非成员函数。

> Symmetric operators—those that might convert either operand, such as the arithmetic, equality, relational, and bitwise operators—usually should be defined as ordinary nonmember functions.

```c++
string s = "world";
string t = s + "!";  // 正确，可以将const char*加到一个string对象中。
string u = "hi" + s;  // 如果“+”是string的成员，则产生错误，因此const char*没有定义该运算符。如果“+”是非成员函数，则"hi" + s等价于operator("hi", s)，"hi"被转换为string，因此正确。
```

当设计一个类时，应该首先思考这个类要提供哪些操作，然后才能思考应该把每个操作设成普通函数还是重载运算符。如果某些操作在逻辑上与运算符相关，则它们适合被定义成重载的运算符：

- 如果类执行IO操作，则定义移位运算符使其与内置类型的IO保持一致。
- 如果类的某个操作检查相等性，则定义```operator==```；如果类有了```operator==```，则通常也应该有```operator!=```。
- 如果类有一个内在的单序操作（single, natural ordering operation），则定义```operator<```，此时也应该定义其他关系运算符。
- 通常情况下重载运算符的返回类型应该与内置版本的返回类型兼容：逻辑运算符与关系运算符应该返回```bool```，算术运算符应该返回一个类类型的值，赋值运算符与复合赋值运算符应该返回左侧运算对象的引用。
- 如果类有算术运算符或位运算符，则通常最好也提供相应的复合赋值运算符。复合赋值运算符的含义应该与内置版本保持一致（例如，重载```+=```运算符应该先执行```+```、然后执行```=```）。

## IO运算符

通常，输出运算符的第一个形参为非常量（向流写入内容会改变其状态）的```ostream```对象的引用（流无法被拷贝），第二个形参通常应该为常量引用（打印对象通常不会改变对象），该常量是需要打印的类类型对象（避免拷贝实参）。为了与其他输出运算符保持一致，输入运算符一般返回```ostream```形参。

```c++
// 为Sales_data类定义输出运算符。
ostream &operator<<(ostream &os, const Sales_data &item) {
    os << item.isbn() << " " << item.units_sold << " " << item.revenue << " " << item.avg_price();
    return os;
}
```

输出运算符应该负责打印对象的内容而非格式，要尽量减少格式化操作，以使得用户控制输出的细节。特别地，输出运算符应该打印换行符，否则用户就无法在对象的同一行内打印描述性文本了。

通常，输入运算符的第一个形参为要读取的流的引用，第二个形参为要读入的非常量的对象的引用。该运算符通常返回给定流的引用。

如果流含有错误类型的数据、读取操作到达end-of-file等会导致输入运算符内发生错误。因此输入运算符要检查读取操作。如果在发生错误前，对象已有部分改变，则需要将对象恢复到合法状态。

一些输入运算符要做更多的数据校验工作。通常情况下，输入运算符应该仅设置```failbit```。设置```eofbit```意味着文件耗尽，设置```badbit```意味着流被破坏，这些错误最好由IO库自己标识。

```c++
// 为Sales_data类定义输入运算符。
istream &operator>>(istream &is, Sales_data &item) {
    double price;
    is >> item.bookNo >> item.units_sold >> price;
    if (is) {  // 检测读取是否成功（不需要逐个检查读取操作）。可能需要更多的数据校验，如检查bookNo是否格式规范，如果不规范，则需要设置流的条件状态以标识失败（即使技术上是成功的）。
        item.revenue = item.units_sold * price;
    } else {
        item = Sales_data();  // 将item回复到原始状态。
    }
    return is;
}
```

IO运算符必须是非成员函数，否则其左侧运算对象要么是要打印的类类型对象，要么其必须是```istream```或```ostream```的成员（这是无法做到的，因为无法给标准库中的类添加任何成员）。

因为IO运算符要读写类的非公有成员，因此一般被声明为友元。

## 算术和关系运算符

### 算术运算符

算术与关系运算符通常为非成员函数，形参通常为常量引用（因为不需要改变运算对象的状态）。

算术运算符通常计算两个运算对象并得到一个新值，这个值有别于这两个运算对象，常常通过一个局部变量计算得到，操作完成后返回该局部变量的副本作为结果。

如果定义了算术运算符，则通常也会定义对应的复合赋值运算符。此时通过复合赋值运算符来定义算术运算符通常会更高效（通过算术运算符来定义复合赋值运算符需要执行更多的工作）。

> When a class has both operators, it is usually more efficient to define the arithmetic operator to use compound assignment:

```c++
// 为Sales_data类定义“+”。
Sales_data operator+(const Sales_data &lhs, const Sales_data &rhs) {
    Sales_data sum = lhs;
    sum += rhs;
    return sum;
}
```

### 相等运算符

通常，类通过定义相等运算符来检验两个对象是否相等（equivalent）。如果对应成员相等，则认为两个对象相等。

```c++
// 为Sales_data类定义“==”与“!=”。
bool operator==(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.isbn() == rhs.isbn() && lhs.units_sold == rhs.units_sold && lhs.revenue == rhs.revenue;
}
bool operator!=(const Sales_data &lhs, const Sales_data &rhs) {
 return !(lhs == rhs);
}
```

以下是定义相等运算符的一些设计准则：

- 如果类有判断两个对象是否相等的操作，则应该定义```operator==```而非普通的命名函数。
- 如果类定义了```operator==```，则通常应该判断两个对象是否包含相等（equivalent）的数据。
- 相等运算符应该具有传递性，即如果```a == b```、```b == c```，则```a == c```。
- 如果类定义了```operator==```，则也应该```operator!=```。
- 相等运算符与不相等运算符中的一个完成实际的对象比较操作，而另一个则将其工作委托给完成实际的比较操作的运算符，即调用该运算符。

### 关系运算符

定义了相等运算符的类也常常（不总是）定义关系运算符。特别地，关联容器和一些算法要用到小于运算符，因此定义```operator<```会有用。

通常情况下关系运算符应该：

- 定义一个顺序关系，其与关联容器对关键字的要求一致。
- 如果类定义了```==```，则关系运算符应该与其保持一致，特别地，如果两个对象不相等（```!=```），则其中一个对象应该小于（```<```）另一个对象。

```c++
// Sales_data类不应该定义关系运算符。如果基于比较ISBN来比较两个对象，则违反上述第二个要求。如果还考虑revenue与units_sold成员，则不违反上述要求，但逻辑上不合理（不存在单一逻辑可靠（single logical definition）的“<”定义），特别地，无法确定revenue与units_sold哪个应该位于字典序的前面。
```

## 赋值运算符

拷贝赋值运算符与移动赋值运算符是特殊的赋值运算符，除此以外，类可定义其他类型的赋值运算符。

为了与作用于内置类型的赋值运算符、拷贝赋值运算符与移动赋值运算符保持一致，重载的赋值运算符一般返回左侧运算对象的引用。

```c++
vector<string> v;
v = {"a", "an", "the"};  // vector定义的赋值运算符接受花括号列表作为参数。
```

```c++
// 为StrVec类定义赋值运算符，接受花括号列表作为参数。
class StrVec {
public:
    StrVec &operator=(initializer_list<string>);
    // ...
};
StrVec &StrVec::operator=(initializer_list<string> il) {  // 不需要检查自赋值（self-assignment）的情况。
    auto data = alloc_n_copy(il.begin(), il.end());
    free();
    elements = data.first;
    first_free = cap = data.second;
    return *this;
}
```

### 复合赋值运算符

为了与作用于内置类型的复合赋值运算符保持一致，重载的复合赋值运算符一般返回左侧运算对象的引用。

```c++
// 为Sales_data类定义“+=”。
Sales_data &Sales_data::operator+=(const Sales_data &rhs) {
    units_sold += rhs.units_sold;
    revenue += rhs.revenue; 
    return *this;
}
```

## 下标运算符

表示容器的类可以通过元素在容器中的位置访问元素，这些类可定义```operator[]```。

> Classes that represent containers from which elements can be retrieved by position often define the subscript operator, ```operator[]```.

为了与下标运算符的原始定义保持一致，下标运算符通常返回所访问元素的引用，这样下标可以出现在赋值运算符的任意一端。通常最好定义两个版本的下标运算符：一个版本返回普通引用，一个版本作用于常量对象并返回常量引用以保证返回对象不会被赋值。

```c++
// 为StrVec类定义下标运算符。
class StrVec {
public:
    string &operator[](size_t n) {
        return elements[n];
    }
    const string &operator[](size_t n) const {
        return elements[n];
    }
private:
    string *elements;
};

const StrVec cvec = svec;  // svec为StrVec。
const StrVec cvec = svec;
if (svec.size() && svec[0].empty()) {
    svec[0] = "zero";
    cvec[0] = "Zip";  // 错误：返回的是常量引用。
}
```

## 递增与递减运算符

如果要为类定义递增与递减运算符，则应该同时定义前置与后置版本。

为了与内置类型的运算符保持一致，前置递增与递减运算符应该返回递增或递减后的对象的引用。

```c++
// 为StrBlobPtr类定义前置递增与递减运算符。

class StrBlobPtr {
    StrBlobPtr &operator++();
    StrBlobPtr &operator--();
    // ...
};

StrBlobPtr &StrBlobPtr::operator++() {
    // 先check再递增。
    check(curr, "increment past end of StrBlobPtr");
    ++curr;
    return *this;
}
StrBlobPtr &StrBlobPtr::operator--() {
    // 先递减再check。如果curr本来为0，则递减后变成一个很大的正数。
    --curr;
    check(curr, "decrement past begin of StrBlobPtr");
    return *this;
}
```

后置版本的递增与递减运算符接受一个```int```类型的形参。当使用后置运算符时，编译器为其提供值为0的实参。尽管后置运算符可以使用这个形参，但实际中通常不会这么做。该形参只用作区分前置与后置版本。

为了与内置类型的运算符保持一致，后置递增与递减运算符应该返回旧值（不是旧值的引用）。

```c++
// 为StrBlobPtr类定义后置递增与递减运算符。

class StrBlobPtr {
    StrBlobPtr operator++(int);
    StrBlobPtr operator--(int);
};

StrBlobPtr StrBlobPtr::operator++(int) {
    StrBlobPtr ret = *this;
    ++*this;  // 调用前置版本的递增运算符。
    return ret;
}

StrBlobPtr StrBlobPtr::operator--(int) {
    StrBlobPtr ret = *this;
    --*this;  // 调用前置版本的递减运算符。
    return ret;
}
```

可以显式调用后置运算符，此时必须为其整型实参提供一个值。

```c++
StrBlobPtr p(a1);
p.operator++(0);  // 调用前置版本的operator++。
p.operator++();  // 调用后置版本的operator++。
```

## 成员访问运算符

和其他运算符一样，可以使用```operator*```完成任何我们想要的操作（尽管这么做不好），但是箭头运算符不能丢掉“成员访问”这个基本含义。

对于```point -> mem```，```point```必须为指向类对象的指针或一个重载了```operator ->```的类对象。其执行过程如下：

1. 如果```point```为内置的指针类型，则其等价于：```(*point).mem```。
2. 如果```point```为类类型对象，则其等价于：```point.operator() -> mem```。此时如果结果为指针，则执行第1步；如果结果为重载了```operator->```的对象，则对该对象递归调用当前步骤。

因此，重载的```->```必须返回指向类对象的指针或自定义了```->```的某个类的对象。

对于```point -> mem```，如果```point```为内置的指针类型，其等价于：```(*point).mem```；如果```point```为类类型对象，则其等价于```point.operator() -> mem```，否则代码错误。

```c++
class StrBlobPtr {
public:
    string &operator*() const {
        auto p = check(curr, "dereference past end");
        return (*p)[curr]; 
    }
    string *operator->() const {
        return &this->operator*();
    }
    // ...
};

StrBlob a1 = {"hi", "bye", "now"};
StrBlobPtr p(a1);
*p = "okay";
cout << p -> size() << endl;  // 打印4。
cout << (*p).size() << endl;  // 等价于p -> size()。
```

## 函数调用运算符

如果类重载了函数调用运算符，则我们可以像使用函数一样使用该类的对象。因为这样的类本身可以存储状态，因此与普通函数相比它们更加灵活。

如果类定义了调用运算符，则该类的对象被称为**函数对象（function object）**，函数对象是一类可调用对象。。

```c++
// 重载函数调用运算符，返回参数的绝对值。
struct absInt {
    int operator()(int val) const {
        return val < 0 ? -val : val;
    }
};

int i = -42;
absInt absObj;
int ui = absObj(i);
```

和其他类一样，函数对象类也可以包含其他成员。函数对象类经常含有一些数据成员被用来定制（customize）调用运算符中的操作。

```c++
// 重载函数调用运算符，打印string，允许用户自定义流与分隔符。
class PrintString {
public:
    PrintString(ostream &o = cout, char c = ' '): os(o), sep(c) {}
    void operator()(const string &s) const {
        os << s << sep;
    }
private:
    ostream &os;
    char sep;
};

PrintString printer;
printer(s);  // 在cout中打印s，后面跟随一个空格。
PrintString errors(cerr, '\n');
errors(s);  // 在cerr中打印s，后面跟随一个换行符。
```

函数对象最常用作泛型算法的实参。

```c++
for_each(vs.begin(), vs.end(), PrintString(cerr, '\n'));  // vs为vector<string>。
```

### 标准库定义的函数对象

标准库定义了一组表示算术运算符、关系运算符与逻辑运算符的类。每个类定义了一个执行命名操作的调用运算符。这些类都被定义为模板，可以为其指定具体的单一类型，类型即调用运算符的形参类型。包括以下类型：```plus```、```minus```、```multiplies```、```divides```、```modulus```、```negate```、```equal_to```、```not_equal_to```、```greater```、```greater_equal```、```less```、```less_equal```、```logical_and```、```logical_or```、```logical_not```。

```c++
plus<int> intAdd;
negate<int> intNegate;
int sum = intAdd(10, 20);
sum = intNegate(intAdd(10, 20));
sum = intAdd(10, intNegate(10));
```

可以在泛型算法中使用标准库函数对象。

```c++
sort(svec.begin(), svec.end(), greater<string>());  // svec为vector<string>。
```

标准库函数对象适用于指针。

```c++
vector<striing*> nameTable;
sort(nameTable.begin(), nameTable.end(), [](string *a, string *b) { return a < b; });  // 错误，直接比较两个无关指针是未定义的。
sort(nameTable.begin(), nameTable.end(), less<string*>());  // 正确，标准库保证less可以作用于指针。
```

关联容器使用```less<key_type>```对（关键字类型为```key_type```的）元素排序，因此可以定义直接定义一个指针的```set```或关键字为指针的```map```（不用直接指定```less```）。

### ```function```

C++中有好几种可调用对象：函数、函数指针、lambda表达式、```bind```创建的对象以及函数对象等。每个可调用对象都有一种类型，不同类型的可调用对象可能共享一种**调用形式（call signature）**。调用形式指定了对象调用的返回类型与必须传递给调用的实参类型。一种调用形式对应一个函数类型。

> The call signature specifies the type returned by a call to the object and the argument type(s) that must be passed in the call. A call signature corresponds to a function type.

```c++
// 三个具有相同调用形式的可调用对象。
// 调用形式都是：int(int, int)。

int add(int i, int j) {
    return i + j;
}

auto mod = [](int i, int j) { return i % j; };

struct div {
    int operator()(int denominator, int divisor) {
        return denominator / divisor;
    }
};
```

标准库类型```function```可以用来表示特定的调用形式，其定义在头文件```functional```中，并支持以下操作，其中```f```为```function```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>function&lt;T&gt; f</td>
        <td rowspan="3">f为存储可调用对象的空function，可调用对象的调用形式为T。在第三种形式中，f存储可调用对象obj。</td>
    </tr>
    <tr>
        <td>function&lt;T&gt; f(nullptr)</td>
    </tr>
    <tr>
        <td>function&lt;T&gt; f(obj)</td>
    </tr>
    <tr>
        <td>f</td>
        <td>如果f含有一个可调用对象，返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>f(args)</td>
        <td>使用args作为实参调用f。</td>
    </tr>
</table>
```function```定义的类型成员包括：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>result_type</td>
        <td>该function类型的可调用对象的返回类型。</td>
    </tr>
    <tr>
        <td>argument_type</td>
        <td rowspan="3">当T有一个或两个实参时定义的类型。如果T只有一个实参，则argument_type是该类型的同义词；如果T有两个实参，则first_argument_type与second_argument_type是这些实参类型的同义词。</td>
    </tr>
    <tr>
        <td>first_argument_type</td>
    </tr>
    <tr>
        <td>second_argument_type</td>
    </tr>
</table>

```c++
// 使用map定义一个函数表（function table）。
// 通过函数表构建简单的桌面计算计算器。
/* 以下是错误的构建方式，因为不同调用形式的可调用对象类型未必相同：
 * map<string, int(*)(int, int)> binops;
 * binops.insert({"+", add});
 * binops.insert({"%", mod});  // 错误。
 */

map<string, function<int, (int, int)>> binops;  // 如果定义成map<string, int(*)(int, int)，则只能存储函数（指针）。
map<string, function<int(int, int)>> binops = {
    {"+", add},
    {"-", minus<int>()},
    {"/", div()},
    {"*", [](int i, int j) { return i * j; }},
    {"%", mod}
};

binops["+"](10, 5);
binops["-"](10, 5);
binops["/"](10, 5);
binops["*"](10, 5);
binops["%"](10, 5);
```

不能直接将重载函数的名字存入```function```类型对象中，因为会产生歧义。此时可以存储函数指针或使用lambda表达式。

```c++
int add(int i, int j) {
    return i + j;
}
Sales_data add(const Sales_data&, const Sales_data);
map<string, function<int(int, int)> binops;
binops.insert({"+", add});  // 错误。

int (*fp)(int, int) = add;
binops.insert({"+", add});  // 正确。

binops.insert({"+", [](int a, int b) { return add(a, b); }});  // 另一种方法。
```

## 类型转换运算符

**类型转换运算符（conversion operator）**是特殊的成员函数，负责将一个类类型的值转换为其他类型的值，一般形式为：```operator type() const```。其中，```type```为目标类型，只要该类型能作为函数的返回类型（除了```void```）。因此不允许转换成数组类型或函数类型，但能转换为对应的指针或引用类型。

类型转换运算符没有显式的返回类型，也没有形参，而且必须定义成类的成员函数。类型转换运算符通常不应该改变要转换的对象的内容，因此一般被定义成const。

转换构造函数与类型转换运算符共同定义了**类类型转换（class-type conversion）**，有时也被称为**用户定义的类型转换（user-defined conversion）**。

```c++
class SmallInt {
public:
    SmallInt(int i = 0): val(i) {
        if (i < 0 || i > 255) {
            throw out_of_range("Bad SmallInt value");
        }
    }
    operator int() const { return val; }
private:
    size_t val;
};

SmallInt si;
si = 4;  // 首先将4隐式转换为SmallInt。
si + 3;  // 首先将si转换为int，然后执行整数加法。
```

编译器一次只会一个用户定义的类型转换，但是一个内置类型转换可以置于其前或其后。

```c++
SmallInt si = 3.14;  // 首先将3.14转换为int。
si + 3.14;  // 将si转换为int，再转换为double。
```

尽管类型转换运算符不指定返回类型，但每个转换构造函数必须返回对应的类型值。

```c++
class SmallInt;
operator int(SmallInt&);  // 错误，不是成员函数。
class SmallInt {
public:
    int operator int() const;  // 错误，指定了返回类型。
    operator int(int = 0) const;  // 错误，有形参。
    operator int*() const {
        return 42;  // 错误，42不是指针。
    }
};
```

实践中类很少提供转换运算符，如果类型转换自动发生，则用户可能会感到意外，而不是感觉受到了帮助。一个例外是定义向```bool```的类型转换是比较普遍的现象。

类型转换运算符可能产生意外结果。

```c++
int i = 42;
cin << i;  // istream的bool转换运算符将cin转换为bool，该bool值又被提升为int。导致该语句（莫名其妙地）可以运行。在C++标准的早期版本中，IO类型定义了一个向void*的转换来避免该问题。现在，IO标准库通过定义一个向bool的显式类型转换实现同样的目的。
```

为了防止异常的转换发生，通过将类型转换运算符声明为explicit的，可以阻止隐式转换，这被称为**显式的类型转换运算符（explicit conversion operator）**。

```c++
class SmallInt {
    explicit operator int() const {
        return val;
    }
    // ...
};

SmallInt si = 3;
si + 3;  // 错误。
static_cast<int>(si) + 3;  // 正确，可以显式地请求转换。
```

显式的类型转换运算符的一个例外情况是：如果表达式被用作条件（```if```、```while```、```do```语句的条件部分；```for```语句头的条件表达式；逻辑运算符的运算对象、条件运算符的条件表达式等），则编译器会将显式转换（隐式地）应用于它。向```bool```的转换通常用于条件中，因此```operator bool```通常应该被定义为```explicit```的。

```c++
while (cin >> value)  // 使用为IO类型定义的（explicit的）operator bool。
```

明智使用转换运算符可以大大简化类设计者的工作，也能使得使用类更加容易。

如果类类型和转换类型间不存在明显的单射（single mapping）关系，则转换运算符具有误导性。例如，因为日期与```int```之间不存在单一的一一映射关系（一个```int```既可以与年-月-日的十进制标识映射，如“July 30, 1989”可以映射为19890730；也可以与从某个时间节点开始经过的天数映射），因此表示日期的```Date```类型与```int```之间不定义类型转换更好，应该定义一个或多个普通成员函数从各种不同形式中提取所需信息。

### 避免有二义性的转换

有两种方式会导致多重转换路径：两个类提供相同（mutual）的转换；一个类定义多个转换，而它们的转换源或转换目标类型可以通过转换联系在一起。

```c++
// 两个类提供相同的转换会导致二义性。
// 通常不要为类定义相同类型的转换。

struct A {
    A() = default;
    A(const B&);  // 将B转换为A。
    // ...
};
struct B {
    operator A() const;  // 也是将B转换为A。
    // ...
};

A f(const A&);
B b;
A a = f(b);  // 二义性错误：是f(B::operator A())还是f(A::A(const B&))？

// 显式转换运算符或构造函数。
// 注意无法使用强制类型转换来解决二义性问题，因为其本身也有同样的二义性。
A a1 = f(b.operator A());
A a2 = f(A(b));
```

```c++
// 一一个类定义多个转换，而它们的转换源或转换目标类型可以通过转换联系在一起会导致二义性。
// 通常不要在类中定义两个转换源或转换目标是算术类型的转换。

struct {
    A(int = 0);
    A(double);
    operator int() const;
    operator double() const;
    // ...
};

void f2(long double);
A a;
f2(a); // 二义性错误：f(A::operator int())还是f(A::operator double())？
long lg;
A a2(lg);  // 二义性错误：A::A(int)还是A::A(double)？
```

当使用两个用户定义的类型转换时，如果转换函数之前或之后存在标准转换，则标准转换将决定最佳匹配是哪个。

```c++
short s = 42;
A a3(s);  // 使用A::A(int)。
```

当调用重载的函数时，如果多个转换如果多个转换都提供了可行匹配，则这些转换的优先级一样。在该过程中，任何可能出现的标准类型转换的优先级都不被考虑，除非这些重载函数能通过同一用户定义的类型转换得到匹配。

```c++
struct C {
    C(int);
    // ...
};
struct D {
    D(int);
    // ...
};

void manip(const C&);
void manip(const D&);
manip(10); // 二义性错误：manip(C(10))还是manip(D(10))？

manip(C(10));  // 显式构造正确类型。在调用重载函数时需要使用构造函数或强制类型转换来改变实参类型经常意味着设计缺陷。
```

```c++
struct E {
    E(double);
    // ...
};

void manip2(const C&);
void manip2(const E&);
manip2(10);  // 错误：manip2(C(10))还是manip2(E(double(10)))？
```

当运算符函数被用作表达式时，候选函数集的规模会比我们使用调用运算符调用函数时更大。例如，如果```a```是类类型，则```a + b```可能是```a.operator+(b)```或```operator+(a, b)```。此时候选函数包含该运算符的非成员版本与成员版本（如果左侧运算对象为类类型的话）。

```c++
class SmallInt {
    friend SmallInt operator+(const SmallInt&, const SmallInt&);
public:
    SmallInt(int = 0);
    operator int() const {
        return val;
    }
    private:
    	size_t val;
};

SmallInt s1, s2;
SmallInt s3 = s1 + s2;  // 使用重载的operator+。
int i = s3 + 0;  // 二义性错误。可以将0转换为SmallInt，也可以将s3转换为int。如果对同一个类既提供了转换目标是算术类型的类型转换函数，又提供了重载的运算符，则会遇到选择重载运算符还是内置运算符的二义性问题。
```

以下是设计类型转换运算符的一些经验规则：

- 不要令两个类执行相同的转换。
- 避免转换目标是内置算术类型的类型转换。特别地，如果已经定义了一个转换成算术类型的类型转换，则接下来：
  - 不要再定义接受算术类型的重载运算符。如果用户需要这样的运算符，则转换操作将转换该类型对象，然后使用内置的运算符。
  - 不要定义转换到多种算术类型的类型转换。让标准类型转换完成向其他算术类型转换的工作。

总之，除了显式地向```bool```类型的转换外，应尽量避免定义类型转换函数并尽可能限制那些“显然正确”的非```explicit```构造函数。

# 模板

OOP与**泛型编程（generic programming）**都能处理编写程序时类型未知的情况。不同之处在于：OOP能处理类型在运行前都未知的情况，而在泛型编程中，在编译时就能获知类型了。

容器、迭代器与算法都是泛型编程的例子。当我们编写一个泛型程序时，我们编写不依赖于任何特定类型的代码。当使用泛型程序时，我们提供类型或值，实例或程序可以在其上运行。

> Both object-oriented programming (OOP) and generic programming deal with types that are not known at the time the program is written. The distinction between the two is that OOP deals with types that are not known until run time, whereas in generic programming the types become known during compilation.

> The containers, iterators, and algorithms described in Part II are all examples of generic programming. When we write a generic program, we write the code in a way that is independent of any particular type. When we use a generic program, we supply the type(s) or value(s) on which that instance of the program will operate.

**模板（template）**定义以关键字```template```开始，后跟**模板形参列表（template paramter list）**，这是一个逗号分隔的一个或多个**模板形参（template paramter）**的列表，并用```<```与```>```包围起来。

模板形参可以是模板**类型形参（type paramter）**，由关键字```class```或```typename```紧跟一个标识符组成（一个模板形参列表中可以同时互换使用这两个关键字），一个类型形参表示一个类型。

```c++
template <typename T> T foo(T *p) {
    T tmp = *p;  // tmp的类型为指针p指向的类型。
    // ...
    return tmp;  // 正确，返回类型为T。
}
```

```c++
template <typename T, U> T calc(const T&, const U&);  // 错误，U前必须有class或typename。

template <typename T, class U> T calc(const T&, const U&);  // 正确
```

模板形参也可以是**非类型形参（nontype parameter）**，由一个类型名紧跟一个标识符组成，一个非类型形参表示一个值而非类型。非类型形参可以是整型、只想对象或函数类型的指针或（左值）引用。绑定到非类型整型形参的实参必须是常量表达式，绑定到指针或引用非类型形参的实参必须有静态的生存期，因此不能使用非静态的局部变量或动态对象作为指针或引用非类型模板形参的实参。指针形参也可以被```nullptr```或值为0的常量表达式来实例化。在模板定义中，模板非类型形参是一个常量，因此可以用在需要常量表达式的地方。

当使用模板时，需要（显式或隐式地）指定**模板实参（template argument）**，将其绑定到模板（类型）形参上。

## 实例化

当编译器遇到一个模板定义时，其不会生成代码。只有当模板被**实例化（instantiate）**时（即使用模板时）才会生成代码，此时模板形参被编译器推断出的或显式提供的模板形参所代替。当实例化一个模板时，它使用实际的模板实参替代对应的模板形参来创建模板的“新实例”，这些编译器生成的版本通常称为模板的**实例（instantiation）**。

模板包含两类名字，一类是不依赖于模板形参的名字，一类是依赖于模板形参的名字。当使用模板时，模板的提供者要保证所有所有不依赖于模板形参的名字都是可见的，并保证模板被实例化时，模板的定义（包括类模板的成员的定义）都是可见的。模板的用户要保证用来实例化模板的所有函数、类型以及与类型相关的运算符的声明都是可见的。为了实例化模板，编译器需要知道函数模板或类模板成员函数的定义。为了满足以上要求，模板头文件通常包括（类模板或其成员定义中用到的所有名字的）模板声明与模板定义，而模板的使用者则包含模板头文件，以及用来实例化模板的任何类型的头文件。

通常编译器在三个阶段报告模板错误：

- 模板编译时。此时编译器不会发现很多错误。编译器可以发现语法错误等。
- 编译器遇到模板使用时。此时编译器也不会发现很多错误。对于函数模板，编译器会检查实参数目是否正确、两个实参类型是否相同等；对于类模板，编译器会检查用户是否提供了正确数量的模板实参等。
- 模板实例化时。此时编译器可以发现类型相关错误。一些错误可能在链接时报告，这取决于编译器如何管理实例化。

```c++
template <typename T> int compare(const T &v1, const T &v2) {  // 类型形参。
    if (v1 < v2) {  // 要求类型T的对象支持“<”。
        return -1;
    }
    if (v2 < v1) {  // 要求类型T的对象支持“<”。
        return 1;
    }
    return 0;  // 不依赖于模板形参。
}

Sales_data data1, data2;
cout << compare(data1, data2) << endl;  // 错误，（直到）实例化时发现Sales_data未定义<。
```

### 显式实例化

由于模板被使用时才会实例化，因此相同的实例化可能出现在多个文件中。当多个独立编译的源文件使用了相同的模板，并且这些模板有相同的模板实参，则每个文件中都有一个实例。在大系统中，这可能会造成巨大的开销。**显式实例化（explicit instantiation）**可以避免这种开销，其中显式实例化声明为：```extern template declaration```，显式实例化定义为```template declaration```，其中```declaration```为类或函数的声明，其中所有模板形参被模板实参所替代。

```c++
extern template class Blob<string>;  // 实例化声明。
template int compare(const int&, const int&);  // 实例化定义。
```

当编译器遇到```extern```模板声明时，它不会在本文件中生成实例化代码。将一个实例化声明为```extern```就承诺在程序其他位置有该实例化的非```extern```使用。对于一个给定的实例化版本，可以有多个实例化声明，但是只能有一个实例化定义。

由于编译器在使用一个模板时自动对其实例化，因此```extern```声明必须出现在任何使用此实例化版本的代码之前。

由于编译器遇到一个实例化定义时，它不知道程序会使用哪些成员函数，因此一个类模板的实例化定义会实例化该模板的所有成员，包括内联成员函数。因此，用来显式实例化一个类模板的类型，必须能用于该模板的所有成员。

```c++
// 位于文件Application.cc中。
// 这些模板类型必须在程序其他地方被实例化。
extern template class Blob<string>;
extern template int compare(const int&, const int&);
Blob<string> sa1, sa2;  // 实例化会出现在其他位置。
Blob<int> a1 = {0,1,2,3,4,5,6,7,8,9};  // Blob<int>及其接受initializer_list的构造函数在本文件中被实例化。
Blob<int> a2(a1);  // 拷贝构造函数在本文件中实例化。
int i = compare(a1[0], a2[0]);  // 实例化会出现在其他位置。

// 位于文件templateBuild.cc中。
// 实例化文件必须为每个在其他文件中声明为extern的类型和函数提供一个（非extern）的定义。
template int compare(const int&, const int&);
template class Blob<string>;  // 实例化类模板的所有成员。
```

## 函数模板

将模板作用于函数上得到的是**函数模板（function template）**。

```c++
template <typename T> int compare(const T &v1, const T &v2) {  // 类型形参。
    if (v1 < v2) {
        return -1;
    }
    if (v2 < v1) {
        return 1;
    }
    return 0;
}
```

```c++
template<unsigned N, unsigned M> int compare(const char (&p1)[N], const char (&p2)[M]) {  // 非类型形参。
    return strcmp(p1, p2);
}
```

函数模板可以按照类似普通函数调用的方式被调用（不用显式指定模板实参），此时编译器用推断出的模板形参来实例化特定版本的函数。

```c++
cout << compare(1, 0) << endl;  // 实例化出：int compre(const int&, const int&)。

vector<int> vec1{1, 2, 3}, vec2{4, 5, 6};
cout << compare(vec1, vec2) << endl;  //  实例化出int compare(const vector<int>&, const vector<int>)。
```

```c++
compare("hi", "mom");  // 实例化出：int compre(const char (&p1)[3], const char (&p2)[4])。
```

函数模板可以被声明为```inline```或```constexpr```的，此时```inline```与```constexpr```说明符在模板形参列表后、返回类型前。

要尽量编写类型无关的代码，因此编写模板有两个重要原则：

- 扩大形参的范围。例如模板形参尽可能是```const```的引用，以便函数可以用于不能拷贝的类型和```const```变量。
- 减少对实参类型的要求。例如函数体中尽可能使用```<```比较运算。

```c++
template <typename T> int compare(const T &v1, const T &v2) {  // 类型形参。
    if (v1 < v2) {
        return -1;
    }
    if (v1 > v2) {  // 不推荐的写法：要求T必须定义了“>”。
        return 1;
    }
    return 0;
}
```

```c++
// 如果关心类型无关与可移植性，则可使用less进行比较操作。
// 可以用于指针比较。
// 一种说法认为less<T>默认实现用的就是<，因此这么做并没有起到什么效果。
template <typename T> int compare(const T &v1, const T &v2) {
    if (less<T>()(v1, v2)) {
        return -1;
    }
    if (less<T>()(v2, v1)) {
        return 1;
    }
    return 0;
}
```

## 类模板

将模板作用于类上得到的是**类模板（class template）**。

```c++
// 使用模板扩展StrBlob的元素类型。

template <typename T> class Blob {  // 类模板不是类型名，而是用来实例化一个类型。
public:
    typedef T value_type;
    typedef typename vector<T>::size_type size_type;
    Blob();
    Blob(initializer_list<T> il);
    size_type size() const {
        return data->size();
    }
    bool empty() const {
        return data->empty();
    }
    void push_back(const T &t) {
        data->push_back(t);
    }
    void push_back(T &&t) {
        data->push_back(std::move(t));
    }
    void pop_back();
    T &back();
    T &operator[](size_type i);
private:
    shared_ptr<vector<T>> data;  // 模板实参为Blob的模板形参，不是具体的类型。
};
```

当使用一个模板时，必须提供**显式模板实参（explicit template argument）**，即在类名后面加上```<```与```>```包围起来的模板实参列表。

```c++
/* 实例化出：
 * template <> class Blob<int> {
 *     typedef typename vector<int>::size_type size_type;
 *     // ...
 *  };
 */
Blob<int> ia;
Blob<iint> ia2 = {0, 1, 2, 3, 4};
```

一个类模板的每个实例都是独立的类，一个实例化类与其他实例的类无关，也没有对其他实例的类的特殊的访问权限。

### 类模板的成员函数

与其他类一样，可以在模板内部与外部定义成员函数。定义在模板内的成员函数被隐式声明为内联函数。

类模板的每个实例都有自己版本的成员函数，因此类模板的成员函数与类本身有一样的模板形参，定义在类模板外的成员函数必须以关键字```template```开始，后跟模板形参列表。与往常一样，当在类外定义一个成员时，必须用类作用域运算符限定成员所属类。与往常一样，该类的名字必须包含（显式）模板实参（与模板形参相同）。

```c++
// Blob的成员函数定义。

template <typename T> void Blob<T>::check(size_type i, const string &msg) const {
    if (i >= data->size()) {
        throw out_of_range(msg);
    }
}

template <typename T> T &Blob<T>::back() {
    check(0, "back on empty Blob");
    return data->back();
}
template <typename T> T &Blob<T>::operator[](size_type i) {
    check(i, "subscript out of range");
    return (*data)[i];
}
template <typename T> void Blob<T>::pop_back() {
    check(0, "pop_back on empty Blob");
    data->pop_back();
}

// 构造函数
template <typename T> Blob<T>::Blob(): data(make_shared<vector<T>>()) {}
template <typename T> Blob<T>::Blob(initializer_list<T> il): data(make_shared<vector<T>>(il)) {}
```

默认情况下，类模板的成员函数只有被使用时才进行实例化。如果一个成员函数没有被使用，则其不会被实例化。因此，即使某种类型不满足某些模板操作的要求，也能使用该类型实例化类（例如```vector<A> vec```是合法的，即使类```A```没有定义任何关系运算符）。

```c++
Blob<int> squares = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};  // 实例化出Blob<int>::size() const
for (size_t i = 0; i != squares.size(); ++i) {
    squares[i] = i * i;  // 实例化出Blob<int>::operator[](size_t)
}
```

在类模板自己的作用域内，可以直接使用模板名而不必提供实参。

```c++
// 为Blob定义伴随类BlobPtr。

template <typename T> class BlobPtr {
public:
    BlobPtr(): curr(0) {}
    BlobPtr(Blob<T> &a, size_t sz = 0): wptr(a.data), curr(sz) {}
    T &operator*() const {
        auto p = check(curr, "dereference past end");
        return (*p)[curr];
    }
    BlobPtr &operator++();  // BlobPtr<T> &operator++();
private:
    BlobPtr &operator--();  // BlobPtr<T> &operator--();
    shared_ptr<vector<T>> check(size_t, const string&) const;
    weak_ptr<vector<T>> wptr;
    size_t curr;
};
```

```c++
// BlobPtr的成员函数定义。
template <typename T> BlobPtr<T> BlobPtr<T>::operator++(int) {  // 在使用BlobPtr<T>前，还未进入BlobPtr作用域内，必须包含模板实参。
    BlobPtr ret = *this;  // 已进入BlobPtr作用域内，不必包含模板实参。
    ++*this;
    return ret;
}
```

### 类模板的静态成员

类模板可以声明静态成员。同样地，每个类模板的实例都有一个共享的静态成员。模板的静态成员的定义方式与非静态成员类似。

```c++
template <typename T> class Foo {
public:
    static size_t count() {
        return ctr;
    }
    // ...
private:
    static size_t ctr;
    // ...
}

template <typename T> size_t Foo<T>::ctr = 0;  // 定义并初始化ctr，Foo需要包括模板实参。

Foo<string> fs;  // 实例化静态成员Foo<string>::ctr与Foo<string>::count。
Foo<int> fi, fi2, fi3;  // 共享实例化的静态成员Foo<int>::ctr与Foo<int>::count。
```

与非模板类的静态成员相同，可以通过类类型对象来访问一个类模板的静态成员，也可以通过类作用域运算符直接访问，此时必须引用一个特定的实例。

```c++
Foo<int> fi;
auto ct = Foo<int>::count();
ct = fi.count();
ct = Foo::count();  // 错误。
```

### 模板与友元

当类包含友元声明，类与友元可以与模板独立，也可以不独立。如果类模板包含非模板友元，则友元可以被授权访问所有模板实例；如果友元本身是模板，则类的友元可以授权给所有模板实例，也可以只授权给特定实例。

```c++
// 一对一友元。

template <typename> class BlobPtr;
template <typename> class Blob;
template <typename T> bool operator==(const Blob<T>&, const Blob<T>&);
template <typename T> class Blob {
    friend class BlobPtr<T>;
    friend bool operator==<T> (const Blob<T>&, const Blob<T>&);
    // ...
};

// BlobPtr<char>与operator==<char>为友元。
// BlobPtr<int>与operator==<int>为友元。
```

```c++
template <typename T> class Pal;  // 前置声明。
class C {
    friend class Pal<C>;  // 将Pal<C>作为C的友元，需要前置声明（在作用域内）。
    template <typename T> friend class Pal2;  // 将Pal2的所有实例作为C的友元。
};
template <typename T> class C2 {
    friend class Pal<T>;  // 每个Pal<T>都是C2<T>的友元，需要前置声明（在作用域内）。。
    template <typename X> friend class Pal2;  // Pal2的所有实例都是C2的所有实例的友元。
    friend class Pal3;  // Pal3为非模板类，它是C2所有实例的友元。
};

// To allow all instantiations as friends, the friend declaration must use template parameter(s) that differ from those used by the class itself.
```

可以将模板类型形参作为友元，此时即使使用内置类型来实例化模板也是可以的。

```c++
template <typename Type> class Bar {
    friend Type;
}
```

### 模板类型别名

类模板实例本身定义了一个类类型，因此可以为其定义别名。但是模板本身不是一个类型，因此不能使用```typedef```为模板定义别名。

```c++
typedef Blob<string> StrBlob;  // 正确。
template <typename T> typedef Blob<T> TBlob;  // 错误。
```

但是可以使用```using```为定义类型别名。

```c++
template <typename T> using twin = pair<T, T>;
twin<string> authors;  // authors是一个pair<string, string>。
```

使用模板类型别名时，可以固定一个或多个模板形参。

```c++
template <typename T> using partNo = pair<T, unsigned>;
partNo<string> books;  // books是一个pair<string, unsigned>。
```

## 成员模板

一个类或模板类可以包含本身是模板的成员函数，这种成员被称为**成员模板（member template）**。

成员模板不能是虚函数。

```c++
// （非模板）类的成员模板。
class DebugDelete {
public:
    DebugDelete(ostream &s = cerr): os(s) {}
    template <typename T> void operator()(T *p) const {
        os << "deleting unique_ptr" << endl;
        delete p;
    }
private:
    ostream &os;
};

double *p = new double;
DebugDelete d;
d(p);  // 调用DubugDelete::operator()(double*，释放p。
int *ip = new int;
DebugDelete()(ip);  // 在一个临时的DebugDelete上调用operator()(int*)。

// 将DebugDelete作为unique_ptr的删除器。
unique_ptr<int, DebugDelete> p(new int, DebugDelete());  // 实例化DebugDelete::operator()<int>(int*)。
unique_ptr<string, DebugDelete> sp(new string, DebugDelete());  // 实例化DebugDelete::operator()<string>(string*)。
```

如果类模板含有成员模板，则类和成员各自有自己的、独立的模板形参。此时在类外定义成员模板时，必须依次为类模板与成员模板提供模板形参列表。

```c++
// 为Blob定义新的构造函数，接受一对迭代器表示要拷贝的元素的范围。

template <typename T> class Blob {
    template <typename It> Blob(It b, It e);
    // ...
};

template <typename T> template <typename It> Blob<T>::Blob(It b, It e): data(make_shared<vector<T>>(b, e)) {}
```

为了实例化类模板与成员模板，必须同时提供类与成员函数模板的实参。其中，函数模板的实参同样推断而来。

```c++
int ia[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
vector<long> vi = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
list<const char*> w = {"now", "is", "the", "time"};
Blob<int> a1(begin(ia), end(ia));  // 实例化Blob<int>与接受两个int*形参的构造函数。
Blob<int> a2(vi.begin(), vi.end());  // 实例化Blob<int>与接受两个vector<long>::iterator形参的构造函数。
Blob<string> a3(w.begin(), w.end());  // 实例化Blob<string>与接受两个list<const char*>::iterator形参的构造函数。
```

## 模板形参

模板形参遵循普通的作用域规则。一个模板名可用于声明后、模板声明或定义结束前。模板形参同样会隐藏外层作用域中声明的相同名字。模板形参名不能在模板内被重用，这也意味着一个模板形参名只能在特定模板形参列表中出现一次。

```c++
typedef double A;
template <typename A, typename B> void f(A a, B b) {
    A tmp = a;  // 这里的A对应模板形参，而不是double。
    double B;  // 错误，重声明模板形参B。
}
template <typename V, typename V> void g() {  // 错误。
    // ...
}
```

模板声明必须包括模板形参。

```c++
// 声明但不定义模板。
template <typename T> int compare(const T&, const T&);
template <typename T> class Blob;
```

与函数形参相同，声明中的模板形参的名字不必与定义中的相同。当然，模板声明与定义必须有相同数量与种类（即类型或非类型）的形参。

```c++
// 3个calc都指向相同的函数模板。
template <typename T> T calc(const T&, const T&);
template <typename U> U calc(const U&, const U&);
template <typename Type> Type calc(const Type &a, const Type &b) {
    /* . . . */
}
```

默认情况下，C++假设通过作用域运算符访问的名字不是类型。如果想要使用一个模板类型形参的类型成员，则必须使用```typename```关键字显式地告诉编译器该名字是一个类型。

```c++
T::size_type *p;  // T为模板形参。这是一个名为p的变量还是一个名为size_type的静态数据成员与名为p的变量相乘？C++认为是后者。

template <typename T> typename T::value_type top(const T &c) {  // 显式地告诉编译器这是T::value_type是类型。
    if (!c.empty()) {
        return c.back();
    }
    else {
        return typename T::value_type();  // 显式地告诉编译器这是T::value_type是类型。
    }
}
```

类似于函数的默认实参，模板也可以有**默认模板实参（default template argument）**。函数模板与类模板都支持默认实参。

```c++
template <typename T, typename F = less<T>> int compare(const T &v1, const T &v2, F f = F()) {
    if (f(v1, v2)) {
        return -1;
    }
    if (f(v2, v1)) {
        return 1;
    }
    return 0;
}

bool i = compare(0, 42);  // 使用less<int>比较。

Sales_data item1(cin), item2(cin);
bool j = compare(item1, item2, compareIsbn);
```

无论何时使用类模板，必须在模板名后接上尖括号，其指出类从一个模板实例化而来；即使类模板为所有模板形参提供了默认实参，并且我们希望使用这些默认实参，（空）尖括号也不可省略。

```c++
template <class T = int> class Numbers {
public:
    Numbers(T v = 0): val(v) {}
    // ...
private:
    T val;
};

Numbers<long double> lots_of_precision;
Numbers<> average_precision;
```

## 模板实参推断

通过函数实参来确定模板实参的过程被称为**模板实参推断（template argument deduction）**。

在模板实参推断中，与往常一样，顶层```const```在形参与实参中被忽略。除此以外，模板实参推断仅支持以下类型转换：

- （底层）```const```转换。
- 数组或函数指针转换：如果数组或函数形参不是引用类型，则可以应用正常的指针转换。

```c++
template <typename T> T fobj(T, T);
template <typename T> T fref(const T&, const T&);

string s1("a value");
const string s2("another value");
fobj(s1, s2);  // 调用fobj(string, string)，顶层const被忽略。
fref(s1, s2);  // 调用fref(const string&, const string&)。

int a[10], b[42];
fobj(a, b);  // 调用f(int*, int*)。
fref(a, b);  // 错误，数组类型不匹配。
```

```c++
long lng;
compare(lng, 1024);  // 错误，long无法转换为int。

template <typename A, typename B> int flexibleCompare(const A &v1, const B &v2) {
    if (v1 < v2) {
        return -1;
    }
    if (v2 < v1) {
        return 1;
    }
    return 0;
}

long lng;
flexibleCompare(lng, 1024);  // 正确。
```

正常类型转换应用于不涉及模板类型形参的类型。

```c++
template <typename T> ostream &print(ostream &os, const T &obj) {
    return os << obj;
}

print(cout, 42);  // 实例化print(ostream&, int)。
ofstream f("output");
print(f, 10);  // 同样实例化print(ostream&, int)。
```

### 显式模板实参

类似于类模板，函数模板实例化时也可以显式指定模板实参，这被称为**显式模板实参（explicit template argument）**，这与定义类模板实例的方式相同，即在函数名名后面加上```<```与```>```包围起来的模板实参列表。

```c++
template <typename T1, typename T2, typename T3> T1 sum(T2, T3);  // 实例化时，编译器无法推断T1的类型。

auto val3 = sum<long long>(i, lng);  // i为int。显式指定T1的类型。
```

显式模板实参按从左到右的顺序与对应的模板形参匹配，只有尾部的模板形参对应的模板实参可以被忽略（前提是它们可以从函数形参推断出来）。

```c++
// 糟糕的设计，用户必须显式指定三个模板形参。
template <typename T1, typename T2, typename T3> T3 alternative_sum(T2, T1);

auto val3 = alternative_sum<long, long>(i, lng);  // 错误。
auto val2 = alternative_sum<long long, int, long>(i, lng);  // 正确。
```

正常类型转换应用于显式指定的实参。

```c++
long lng;
compare(lng, 1024);  // 错误，long无法转换为int。
compare<long>(lng, 1024);  // 正确，实例化compare(long, long)。
compare<int>(lng, 1024);  // 正确，实例化compare(int, int)。
```

### 尾置返回类型

与函数一样，模板也可以提供尾置返回类型。

```c++
template <typename It> auto fcn(It beg, It end) -> decltype(*beg) {  // It为迭代器类型，尾置返回类型使用了函数形参中。
 // ...
 return *beg;  // 返回序列中元素的引用。
}

vector<int> vi = {1, 2, 3, 4, 5};
Blob<string> ca = { "hi", "bye" };
auto &i = fcn(vi.begin(), vi.end());  // 返回int&。
auto &s = fcn(ca.begin(), ca.end());  // 返回string&。
```

### 函数指针与实参推断

当使用函数模板来初始化函数指针或为其赋值，编译器使用指针的类型来推断模板实参。

```c++
template <typename T> int compare(const T&, const T&);
int (*pf1)(const int&, const int&) = compare;  // pf1指向int compare(const int&, const int&)。
```

上下文必须保证每个模板形参有唯一确定的类型或值。如果不能从函数指针类型推断出模板实参，则产生错误。如果因为二义性导致错误，则可以使用显式模板实参来消除歧义。

```c++
void func(int(*)(const string&, const string&));
void func(int(*)(const int&, const int&));
func(compare);  // 二义性错误。

func(compare<int>);  // 正确。
```

### 模板实参推断与引用

当函数形参类型为左值模板类型形参的引用时，则编译器会应用正常的引用绑定规则；如果函数形参同时是```const```的，注意此时```const```是底层的。

当一个函数形参是模板类型形参的普通的左值引用时，引用绑定规则指出只能传递给它一个左值。实参可以是```const```的，也可以是非```const```的。如果实参是```const```的，则模板形参```T```被推断为```const```的。

```c++
template <typename T> void f1(T&);
f1(i);  // i为int，T为int。
f1(ci);  // ci为const int，T为const int。
f1(5);  // 错误，5不是左值。
```

如果函数形参类型为const T&，引用绑定规则指出能给它传递任何类型的实参。此时，如果函数形参本身是const，则T的类型推断结果不是const类型，const也不是模板形参类型的一部分，因为const已经是函数形参类型的一部分。

```c++
template <typename T> void f2(const T&);
f2(i);  // i为int，T为int。
f2(ci);  // ci为const int，T为int。
f2(5);  // T为int。
```

当一个函数形参为右值引用，则正常的引用绑定规则指出能给它传递一个右值，此时推断出的T的类型为该右值实参的类型。

```c++
template <typename T> void f3(T&&);
f3(42);  // T为int。
```

#### 引用折叠

如果间接创建一个引用的引用，则引用会“折叠”，折叠规则如下：

- ```X& &```、```X& &&```与```X&& &```都被折叠为```X&```。
- ```X&& &&```折叠为```X&&```。

引用折叠只能应用于间接创建的引用的引用，如类型别名或模板形参。

引用折叠意味着：如果一个函数形参为模板类型形参的右值引用，则可以传递给它任意类型的实参。特别地，其可以被绑定到左值，且如果实参为左值，则推断出的模板实参类型为左值引用，且函数形参被实例化为普通的左值引用。

```c++
f3(i);  // i为int，T为int&。
f3(ci);  // ci为const int，T为const int&。
```

由于模板形参可以被推断为引用类型（也可以是非引用类型），因此它会影响到赋值或初始化操作的含义。当代码中涉及的类型可能是非引用类型，也可能引用类型时，编写正确的代码就变得异常困难。

```c++
template <typename T> void f3(T &&val) {
    T t = val;  // 拷贝还是绑定一个引用？
    t = fcn(t);  // 赋值只改变t还是同时改变t与val？
    if (val == t) {  // 若T为引用，则总是true。
        /* ... */
    }
}
```

在实际中，右值引用通常两种情况：模板实参转发与模板重载。

使用右值引用的函数模板通常使用如下重载方式，这与为成员函数同时提供拷贝与移动版本的方式相同。

> For now, it’s worth noting that function templates that use rvalue references often use overloading in the same way as we saw in § 13.6.3 (p. 544):

```c++
template <typename T> void f(T&&);  // 绑定到非const的右值。
template <typename T> void f(const T&);  // 绑定到左值与const的右值。
```

### 转发

某些函数需要将一个或多个实参连同类型不变地转发（forward）给其他函数，转发需要保持被转发实参的所有信息。如果要实现转发，需要将函数形参定义为模板形参类型```T```的右值引用，同时将调用函数的形参作为```std::forward<T>```的实参。

```c++
// 实现转发的一般方式。
template <typename Type> intermediary(Type &&arg) {
    /* 如果实参为右值，则Type为非引用类型，forward<Type>返回Type&&。
     * 如果实参为左值，则Type为左值引用，forward<Type>返回Type& &&，折叠为Type&。
     */
    finalFcn(std::forward<Type>(arg));
    // ...
}
```

```c++
// 一个函数模板，接受一个可调用表达式与两个额外实参。函数模板调用可调用对象，并将两个实参逆序传递给它。

// 版本1。
// 丢失了引用与顶层const属性。
template <typename F, typename T1, typename T2> void flip1(F f, T1 t1, T2 t2) {
    f(t2, t1);
}

void f(int v1, int &v2) {
    cout << v1 << " " << ++v2 << endl;
}
f(42, i);  // f改变了实参int i。
flip1(f, j, 42);  // 实例化为：void flip1(void(*fcn)(int, int&), int t1, int t2)。通过flip1调用f不会改变j。


// 版本2。
// 保持了引用与const属性，但是无法调用含有右值引用形参的函数。
template <typename F, typename T1, typename T2> void flip2(F f, T1 &&t1, T2 &&t2) {
    f(t2, t1);
}

void g(int &&i, int &j) {
    cout << i << " " << j << endl;
}
flip2(g, i, 42);  // 错误，42被传递给t2，而t2为左值（函数形参为左值），不能被传递给i。

// 版本3。
// 实现转发的要求：通过std::forward保持给定实参的左值/右值属性。
template <typename F, typename T1, typename T2> void flip(F f, T1 &&t1, T2 &&t2) {
    f(std::forward<T2>(t2), std::forward<T1>(t1));
}
```

## 模板与重载

函数模板可以与另一个函数模板或普通非模板函数重载。与往常一样，重载的函数或函数模板必须有不同数量或类型的形参。

当涉及到函数模板，函数匹配规则受到以下方面的影响：

- 对于一次调用，候选函数包括任何模板实参推断成功的函数模板实例。
- 候选的函数模板总是可行的，因为模板实参推断会排除任何不可行的模板。
- 所有可行转换按照类型转换来排序。需要注意可用于函数模板的类型转换十分有限。
- 如何一个函数提供了比其他函数更好的匹配，则选择该函数；如果有多个函数提供了同样好的匹配，则
  - 如果同样好的函数中只有一个是非模板函数，则选择此函数。
  - 如果同样好的函数中没有非模板函数，而是有多个函数模板，且其中一个模板比其他模板更加特例化（specialized），则选择此模板。
  - 否则调用有歧义。

```c++
// 版本1。
template <typename T> string debug_rep(const T &t) {
    ostringstream ret;
    ret << t;
    return ret.str();
}


// 版本2。
template <typename T> string debug_rep(T *p) {
    ostringstream ret;
    ret << "pointer: " << p;
    if (p) {
        ret << " " << debug_rep(*p);
    } else {
        ret << " null pointer";
    }
    return ret.str();
}

string s("hi");
cout << debug_rep(s) << endl;  // 只有版本1可行。

cout << debug_rep(&s) << endl;  // 两个版本都可行，但是版本1需要底层const转换，版本2精确匹配。因此选择版本2。

const string *sp = &s;
cout << debug_rep(sp) << endl;  // 两个版本都精确匹配，但是版本2更特例化。因为版本1可用于（essentially）任何类型，后者只能用于指针类型。
```

```c++
// 版本3。
string debug_rep(const string &s) {
    return '"' + s + '"';
}

string s("hi");
cout << debug_rep(s) << endl;  // 版本1与版本3提供同样好的匹配（底层const转换），但是版本3非模板，因此选择版本3。

cout << debug_rep("hi world!") << endl;  // 版本1与版本2都精确匹配（分别将T绑定到char[10]与const char上），版本3需要从const char*到string的类型转换。由于版本2更特例化，故选择版本2。
```

```c++
// 如果希望将字符指针按string处理，则可定义另外两个非重载版本。

// 版本4。
string debug_rep(char *p) {
    return debug_rep(string(p));
}

// 版本5。
string debug_rep(const char *p) {
    return debug_rep(string(p));
}

template <typename T> string debug_rep(const T &t);
template <typename T> string debug_rep(T *p);

// 如果要求版本4与版本5正确工作，则版本3的声明必须在作用域中，否则会调用错误的版本。
// 可以在定义任何重载函数前，先声明这些函数。That way you don’t have to worry whether the compiler will instantiate a call before it sees the function you intended to call.
string debug_rep(const string &);

string debug_rep(char *p) {
    return debug_rep(string(p));  // 如果版本3的声明不在作用域中，则调用版本1。
}
```

缺少模板声明可能导致调用错误版本的函数。

```c++
template <typename T> string debug_rep(const T &t);
template <typename T> string debug_rep(T *p);
string debug_rep(char *p) {
    return debug_rep(string(p));  // 如果版本3的声明不在作用域中，则返回语句将调用版本1，其中T实例化为string。
}
```

为了避免模板声明的缺失，在定义任何重载集中的函数前，先对重载集中的函数进行声明。这样就不用担心编译器由于未遇到希望调用的函数而实例化一个并非所需的版本。同时，最好将给定文件所需的所有模板声明放置在文件开头并出现于任何使用这些模板的代码前。

> Declare every function in an overload set before you define any of the functions. That way you don’t have to worry whether the compiler will instantiate a call before it sees the function you intended to call.

> For reasons we’ll explain in § 16.3 (p. 698), declarations for all the templates needed by a given file usually should appear together at the beginning of a file before any code that uses those names.

## 可变参数模板

一个**可变参数模板（variadic template）**是一个接受可变数量形参的模板函数或模板类。可变数量的形参被称为**形参包（parameter pack）**。形参包有两种：**模板形参包（template paramter pack）**（表示零个或多个模板形参）与**函数形参包（function parameter pack）**（表示零个或多个函数形参）。

形参包通过省略号指出。在模板形参列表中，```class...```或```typename...```表示模板的类型形参包；一个类型名后跟上```...```表示模板的非类型形参包。在函数形参列表中，如果一个形参的类型是一个模板形参包，则此参数也是一个函数形参包（同样使用```...```表示）。

```c++
template <typename T, typename... Args> void foo(const T &t, const Args& ... rest);  // Args为模板形参包，rest为函数形参包。
```

对于可变参数模板，与往常一样，编译器根据函数实参推断模板形参类型，同时还会推断包中形参的数目。

```c++
int i = 0;
double d = 3.14;
string s = "how now brown cow";

foo(i, s, 42, d);  // 包中有3个形参，实例化出：void foo(const int&, const string&, const int&, const double&)。
foo(s, 42, "hi");  // 包中有2个形参，实例化出：void foo(const string&, const int&, const char[3]&)。
foo(d, s);  // 包中有1个形参，实例化出：void foo(const double&, const string&)。
foo("hi");  // 空包，实例化出：void foo(const char[3]&)。
```

```sizeof...```运算符返回包中的元素数量。

```c++
template<typename ... Args> void g(Args ... args) {
    cout << sizeof...(Args) << endl;
    cout << sizeof...(args) << endl;
}
```

可变参数模板通常是递归的，例如：一次调用处理包中第一个实参，然后用剩余实参调用自身。为了终止递归，需要定义一个非可变参数的函数，该函数必须位于作用域中，否则会无限递归。

```c++
// 版本1。
template<typename T> ostream &print(ostream &os, const T &t) {
    return os << t;
}
// 版本2。
template <typename T, typename... Args> ostream &print(ostream &os, const T &t, const Args&... rest) {
    os << t << ", ";
    return print(os, rest...);
}

/* 递归过程：
 * i绑定t，s与42绑定rest。
 * 递归调用print(cout, s, 42)。此时s绑定t，42绑定rest。
 * 递归调用print(cout, 42)。两个版本提供同样好的匹配，由于版本1更特例化，故调用版本1。
 */
print(cout, i, s, 42);
```

### 包扩展

对于一个形参包，除了获取其大小外，唯一能做的是就是**扩展（expand）**它。当扩展一个包时，还要提供用于每个被扩展的元素的**模式（pattern）**。扩展一个包就是将它分解为组成它的元素并对每个元素应用模式。通过对模式右置```...```来触发扩展操作。

```c++
template <typename T, typename... Args> ostream &print(ostream &os, const T &t, const Args&... rest) {  // 扩展Args，为print生成函数形参列表。对Args的扩展中，编译器将const Args&应用到模板形参包Args中的每个元素，得到一个逗号分隔的零个或多个类型的列表，每个元素形如const type&。
    os << t << ", ";
    return print(os, rest...);  // 扩展rest，为print的调用生成实参列表（包中元素组成的、逗号分隔的列表）。其中模式为rest。
}

print(cout, i, s, 42);  // 实例化为：ostream print(ostream&, const int&, const string&, const int&)。
```

```c++
// 更复杂的扩展形式。
template <typename... Args> ostream &errorMsg(ostream &os, const Args&... rest) {
    // 模式为debug_rep(rest)。
    /* 不要写成：print(os, debug_rep(rest...));
     * 否则在debug_rep的调用中扩展了rest。
     */
    return print(os, debug_rep(rest)...);
}

// 等价于print(cerr, debug_rep(fcnName), debug_rep(code.num()), debug_rep(otherData), debug_rep("otherData"), debug_rep(item));
// 如果扩展写成了：print(os, debug_rep(rest...))，会编译失败，因为没有匹配的debug_rep。
errorMsg(cerr, fcnName, code.num(), otherData, "other", item);
```

可变参数模板通常将其形参转发给其他函数。这类函数的形式通常如下：

```c++
template<typename... Args> void fun(Args&&... args) {  // 将Args扩展为右值引用列表。
    work(std::forward<Args>(args)...);  // work的实参同时扩展了Args与args。由于fun的形参为右值引用，同时使用std::forward传递实参。因此fun接受任何类型的实参并能在调用work时保持实参的类型信息。
}
```

```c++
// 为StrVec设计emplace_back函数。

class StrVec {
public:
    template <class... Args> void emplace_back(Args&&...);
    // ...
};

template <class... Args> inline void StrVec::emplace_back(Args&&... args) {
    chk_n_alloc();
    alloc.construct(first_free++, std::forward<Args>(args)...);  // 同时扩展Args与args。
}

svec.emplace_back(10, 'c');  // 扩展出：std::forward<int>(10), std::forward<char>('c')。
svec.emplace_back(s1 + s2);  // construct得到右值引用实参，使用string的移动构造函数。
```

## 模板特例化

并非总是可以编写单一模板，使得该模板对每个用来实例化该模板的模板实参都是最适合的。某些情况下，对于特定类型，通用的模板定义可能会编译失败或做得不正确；其他情况下，我们也可以利用特定知识来编写更高效的代码，而不是从通用模板实例化。此时可以定义模板的特例化版本。

```c++
// 版本1，可以比较任意两个类型。
template <typename T> int compare(const T&, const T&);

// 版本2，比较字符串字面值。
// 只有传递给其一个字符串字面值或字符数组时，才会调用该版本。而我们期望对字符（串）指针也能调用之。因此不符合期望。
template<size_t N, size_t M> int compare(const char (&)[N], const char (&)[M]);

const char *p1 = "hi", p2 = "mom";
compare(p1, p2);  // 期望调用版本2，但无法将const char*转换为数组的引用，因此调用版本1。
compare("hi", "mom");  // 调用版本2。
```

一个**模板特例化（template specialization）**是模板的一个独立定义，其中一个或多个模板形参被指定为特定的类型。

当特例化一个函数模板时，必须为原模板中的每个模板形参提供实参，此时函数形参类型必须与先前定义的模板中对应的形参类型匹配。为了指出正在特例化一个模板，应该使用```template <>```取代模板定义。

```c++
// 版本3。
// 注意函数形参类型为const char* const&。const char*（被绑定到T）的const版本是一个常量指针，而不是指向const的指针。
template <> int compare(const char* const &p1, const char* const &p2) {
    return strcmp(p1, p2);
}
```

类模板也可以采用方法特例化，此时需要显式指定模板实参。

```c++
// 将hash<Sales_data>声明为Sales_data的友元。
template <class T> class hash;
class Sales_data {
friend class hash<Sales_data>;
    // ...
};

// 一个特例化的hash函数，可作为无序容器的哈希函数。
// 必须在原模板所在的命名空间中特例化它。
// 为了让Sales_data的用户使用该特例化版本，应该在Sales_data的头文件中定义该特例化版本。
namespace std {
    template <> struct hash<Sales_data> {
        typedef size_t result_type;
        typedef Sales_data argument_type;
        size_t operator()(const Sales_data &s) const;
        // 该类使用合成的拷贝控制成员与默认构造函数。
    };
    size_t hash<Sales_data>::operator()(const Sales_data &s) const {
        return hash<string>()(s.bookNo) ^
               hash<unsigned>()(s.units_sold) ^
               hash<double>()(s.revenue);  // 将定义好的哈希函数的任务交给标准库，其计算所有数据成员的哈希值，与Sales_data的operator==函数兼容。
    }
}

unordered_multiset<Sales_data> SDset;  // 假设特例化版本在作用域中，则使用hash<Sales_data>与Sales_data的operator==函数作为哈希函数与比较函数。
```

类模板可以被**部分特例化（partial specialization）**（特例化部分模板形参，或形参的部分方面），其本身是一个模板，用户必须为特例化中不固定（not fixed）的模板形参提供实参。函数模板不可以被部分特例化。

```c++
// remove_reference的实例：采用一个通用版本与两个部分特例化版本完成。

template <class T> struct remove_reference {
    typedef T type;
};
template <class T> struct remove_reference<T&> {
    typedef T type;
};
template <class T> struct remove_reference<T&&> {
    typedef T type;
};

int i;
remove_reference<decltype(42)>::type a;  // 使用原始模板。
remove_reference<decltype(i)>::type b;  // 使用第一个特例化版本。
remove_reference<decltype(std::move(i))>::type c;  // 使用第二个特例化版本。
```

可以只特例化成员函数而不是整个模板。

```c++
template <typename T> struct Foo {
    Foo(const T &t = T()): mem(t) {}
    void Bar() {
        /* ... */
    }
    T mem;
    // ...
};
template<> void Foo<int>::Bar() {
    // ...
}

Foo<string> fs;  // 实例化Foo<string>::Foo()。
fs.Bar();  // 实例化Foo<string>::Bar()。
Foo<int> fi;  // 实例化Foo<int>::Foo()。
fi.Bar();  // 使用特例化版本的Foo<int>::bar()。
```

注意一个特例化本质上是一个模板的一个实例，而不是函数名的一个重载版本。因此特例化不影响函数匹配过程。

```c++
compare("hi", "mom");  // 3个版本都提供同样好的匹配，但调用版本2，其更特例化。如果版本3被定义为普通的非模板函数，则调用版本3。
```

为了特例化一个模板，原模板的声明必须在作用域中。而且在任何使用模板实例的代码前，特例化的声明也必须位于作用域中。对于普通类与函数，丢失声明通常很容易被发现，因为编译会失败。但是，如果丢失了特例化版本的声明，则编译器可以实例化原版本。由于丢失特例化版本时编译器后常常可以实例化原始模板，很容易产生模板及其特例化版本之间的声明顺序导致的错误，而这种错误很难查找。因此模板及其特例化版本应该定义在同一头文件中。 对于给定的模板名，模板的声明应该放在前面，然后是这些模板的特例化版本。

> It is an error for a program to use a specialization and an instantiation of the original template with the same set of template arguments. However, it is an error that the compiler is unlikely to detect.

> Declarations for all the templates with a given name should appear first, followed by any specializations of those templates.

# 异常处理

**异常处理（exception handling）**机制允许程序独立开发的部分能够对运行时出现的问题进行通信并作出相应处理。

> Exception handling allows independently developed parts of a program to communicate about and handle problems that arise at run time. 

注意：

- 异常中断了程序的正常执行流。当异常发生时，调用者请求的一部分计算可能已经完成，另一半尚未完成。这通常意味着对象处理非法（invalid）或不完整（incomplete）状态、资源没有被释放等等。那些在异常处理期间正确（properly）执行“清理”工作的程序被称为异常安全（exception safe）的代码。但是编写异常安全的代码非常困难。如果程序需要处理异常并继续执行，则必须时刻清楚异常会不会发生、异常发生后程序如何确保对象有效、资源无泄漏、程序恢复到合理（appropriate）状态等。

## 抛出异常

程序通过**```throw```表达式（```throw``` expression）**抛出异常，异常检测部分使用```throw```表达式来表示它遇到了无法处理的问题。我们说```throw```**引发（raise）**了异常。

```throw```表达式包含关键字```throw```和紧随其后的一个表达式，其中表达式的类型就是抛出的异常类型。

```c++
Sales_item item1, item2;
cin >> item1 >> item2;
if (item1.isbn() == item2.isbn()) {
    cout << item1 + item2 << endl;
} else {
    cerr << "Data must refer to same ISBN" << endl;
    return -1;
}

// 将条件语句改写为抛出异常形式：
if (item1.isbn() != item2.isbn()) {
    throw runtime_error("Data must refer to same ISBN");
}
cout << item1 + item2 << endl;
```

### 重新抛出

**重新抛出（rethrowing）**使用一条不包含任何表达式的```throw```语句，将异常传递给另外一个```catch```子句（不是```catch```列表中的接下里的```catch```子句，而是将当前异常对象沿着调用链向上传递）。

空的```throw```语句只能出现在```catch```子句中或```catch```语句直接或间接调用的函数内，否则，编译器将调用```terminate```。

> An empty ```throw``` can appear only in a ```catch``` or in a function called (directly or indirectly) from a ```catch```. If an empty ```throw``` is encountered when a handler is not active, ```terminate``` is called.

如果```catch```子句的形参内容被改变且```catch```子句重新抛出异常，则只有当```catch```异常声明是引用类型时对形参所做的改变才会保留并继续传播。

```c++
catch (my_error &eObj) {  // 引用。
    eObj.status = errCodes::severeErr;
    throw;  // 异常对象的status成员为severeErr。
} catch (other_error eObj) {  // 非引用。
    eObj.status = errCodes::badErr;  // 仅仅改变了局部副本。
    throw;  // 异常对象的status成员未改变。
}
```

### 异常对象

**异常对象（exception object）**是一种特殊的对象，编译器使用异常抛出表达式来对异常对象拷贝初始化。因此```throw```语句中的表达式必须拥有完全类型。如果该表达式是类类型，则相应的类必须有一个可访问的析构函数与一个可访问的拷贝或移动构造函数；如果该表达式是数组类型或函数类型，则表达式被转换为相应的指针类型。

异常对象位于由编译器管理的空间中，保证任何被调用的```catch```子句均可访问该空间。当异常被处理完毕后，异常对象被销毁。

根据栈展开，抛出一个指向局部对象的指针几乎肯定是一种错误的行为。如果指针所指对象位于某个块中，而该块在```catch```子句前已经退出了，则意味着在执行```catch```子句前局部对象已被销毁。抛出指针要求在任何对应的处理代码存在的地方，指针所指向的对象都必须存在。

当抛出一条表达式时，该表达式的静态、编译时类型决定了异常对象的类型。如果一条```throw```表达式解引用一个基类指针，而该指针实际指向派生类对象，则抛出的对象被切掉一部分，只有基类部分被抛出。

## 处理异常

程序通过**```try```（```try``` block）**语句块处理异常。```try```语句块以关键字```try```开始，并以一个或多个**```catch```子句（```catch``` clause）**结束。```try```语句块中代码抛出的异常通常会被某个```catch```子句处理，因此```catch```子句也被称为**异常处理代码（exception handler）**。```try```语句块的形式为：

```c++
try {
    program-statements
} catch (exception-declaration) {
    handler-statements
} catch (exception-declaration) {
    handler-statements
} // ...
```

其中```program-statements```为代码；```exception-declaration```为**异常声明（exception declaration）**，表示一个异常对象的声明，如果未用到则可以未命名；```handler-statements```为异常处理代码。

```try```语句块中的```program-statements```组成程序的正常（normal）逻辑，遵循通常的作用域规则。```program-statements```内声明的变量在块外无法访问，特别地，在```catch```子句中无法访问。

> The *program-statements* inside the ```try``` constitute the normal logic of the program. Like any other blocks, they can contain any C++ statement, including declarations. As with any block, variables declared inside a ```try``` block are inaccessible outside the block—in particular, they are not accessible to the ```catch``` clauses.

```c++
while (cin >> item1 >> item2) {
    try {
        // 将两个Sales_item对象相加。
        // 如果失败，则抛出runtime_error异常。
    } catch (runtime_error err) {
        cout << err.what() << "\nTry Again? Enter y or n" << endl;
        char c;
        cin >> c;
        if (!cin || c == 'n') {
            break;
        }
    }
}
```

### 异常声明

异常声明类型必须是完全类型，可以是左值引用，但不能是右值引用。当进入一个```catch```子句后，异常对象会初始化异常声明中的形参（类似于函数的实参传递）。如果```catch```接受的异常与某个继承体系有关，则最好将该```catch```的参数定义为引用类型。

异常声明的静态类型决定```catch```语句所能执行的操作。如果```catch```的参数是基类类型，则```catch```无法使用派生类特有的成员。

### 异常匹配

在异常发生后搜寻```catch```子句的过程中，最终找到的```catch```子句是第一个与异常匹配的```catch```子句（不一定是最佳匹配）。因此，最专门的```catch```子句应该位于```catch```列表的越前端。

> During the search for a matching ```catch```, the ```catch``` that is found is not necessarily the one that matches the exception best. Instead, the selected ```catch``` is the first one that matches the exception at all. As a consequence, in a list of ```catch``` clauses, the most specialized ```catch``` must appear first.

异常声明只允许以下类型转换：

- （底层）```const```转换。
- 派生类到基类的转换。
- 数组与函数转换为对应的指针。

特别地，标准算术类型转换与类类型转换都不允许。

### 捕获所有异常

语句```catch(...)```可捕获所有异常，这样的处理代码被称为**捕获所有语句（catch all）**，该语句通常与重新抛出语句一起使用：```catch```执行当前局部能完成的工作，随后重新抛出异常。如果```catch(...)```与其他```catch```子句一起出现，则```catch(...)```应该位于最后。

```c++
void manip() {
    try {
        // 操作引发并抛出异常。
    } catch (...) {
        // 处理异常。
        throw;
    }
}
```

### 函数```try```语句块

程序执行的任何时刻都可能发生异常。特别地，异常可能发生在处理构造函数初始值的过程中。因为初始值列表抛出异常时函数体内的```try```语句块还未生效，因此构造函数体内的```catch```语句无法处理构造函数初始值列表中抛出的异常。要处理构造函数初始值异常，只能将构造函数写成函数**```try```语句块（function ```try``` block）**的形式：让关键字```try```出现在构造函数初始值列表的```:```以及构造函数体的左花括号（可能为空）前（或析构函数函数体的左花括号前），并将```catch```子句列表放在构造函数体的右花括号后（或析构函数函数体的右花括号后）。

```c++
Blob<T>::Blob(initializer_list<T> il) try : data(make_shared<vector<T>>(il)) {
} catch(const bad_alloc &e) { handle_out_of_memory(e); }
```

函数```try```语句块使得一组```catch```语句同时关联构造函数的初始化阶段（或析构函数的析构阶段）与构造函数（或析构函数）体。

> A function ```try``` block lets us associate a group of catch clauses with the initialization phase of a constructor (or the destruction phase of a destructor) as well as with the constructor’s (or destructor’s) function body.

初始化构造函数的参数时也可能发生异常，这样的异常不属于函数```try```语句块的一部分，而是调用表达式的一部分，并在调用者所在的上下文中被处理。函数```try```语句块只能处理构造函数开始执行后发生的异常。

### 栈展开

异常处理的过程如下：当异常被抛出后，```throw```后的语句将不再执行。如果```throw```出现在一个```try```语句块中，则依次检查与该```try```关联的```catch```子句，如果找到匹配的```catch```，则使用该```catch```处理异常；否则如果该```try```子句块嵌套在其他```try```语句块中，则按类似方法在外层```try```语句块中寻找匹配的```catch```子句。如果没有找到匹配的```catch```子句，终止该函数，并在调用该函数的函数中继续按照类似流程寻找（必须在外层函数的```try```语句块中）。如果还是没有找到匹配的```catch```子句，则新函数也被终止，继续搜索调用它的函数。以此类推，知道找到合适类型的```catch```子句。

上述异常处理过程被称为**栈展开（stack unwinding）**，其过程意味着：

- 沿着调用链的函数可能提前退出。
- 一旦程序开始执行异常处理代码，则沿着调用链创建的对象将被销毁。如果在栈展开过程中退出某个块，则编译器负责确保在这个块中创建的对象被正确销毁。如果某个局部对象是类类型，则该对象的析构函数被自动调用；编译器在销毁内置类型对象时不需要做任何事情。即使异常发生在构造函数中（对象可能只构造了一部分）或数组以及标准库容器元素初始化过程中，也能保证已构造的成员或元素被正确销毁。

析构函数在栈展开的过程中被执行，因此使用类对象分配的资源总是能够正确地释放，同时这也意味着析构函数不应抛出不能被其自身处理的异常（如果抛出异常则将异常放在```try```语句块中，并在析构函数内处理掉）。在实际编程中，析构函数用于释放资源，因此不太可能抛出异常。所有的标准库类型保证它们的析构函数不会引发异常。

> The fact that destructors are run during stack unwinding affects how we write destructors. During stack unwinding, an exception has been raised but is not yet handled. If a new exception is thrown during stack unwinding and not caught in the function that threw it, ```terminate``` is called. Because destructors may be invoked during stack unwinding, they should never throw exceptions that the destructor itself does not handle. That is, if a destructor does an operation that might throw, it should wrap that operation in a ```try``` block and handle it locally to the destructor.

如果一个块分配了资源（如采用```new```分配资源），并且随后异常发生，则释放资源的代码可能未来得及执行。

假设找到了匹配的```catch```子句，则程序执行进入该子句并执行其中的代码，然后找到与该```try```块关联的最后一个```catch```子句，继续执行该子句后的语句。

如果这样的```catch```子句不存在，则程序执行标准库函数```terminate```，该函数的行为与系统有关，但保证停止程序的进一步执行。特别地，如果发生的异常没有（直接或间接地）定义在任何```try```语句块中，则意味着没有匹配的```catch```子句，此时调用```terminate```函数，程序退出。

## ```noexcept```说明

对于用户与编译器来说，预先知道某个函数不会跑出异常很有用。其有助于简化书写调用该函数的代码的任务；如果编译器知道某个函数不会抛出异常，则有时可以执行某些优化操作，如果函数可能抛出异常，这些优化操作将被限制。

通过**```noexcept```说明（```noexcept``` specification）**（将关键字```noexcept```紧跟在函数参数列表后、尾置返回类型类型前）指定某个函数不会抛出异常。我们称使用了```noexcept```异常声明的函数做了**不抛出声明（nothrowing specification）**。

对于一个函数来说，```noexcept```要么出现在函数的所有声明语句与定义语句中，要么一次也不出现。也可以在函数指针的声明与定义中指定```noexcept```。在成员函数中，```noexcept```需要跟在```const```与引用限定符后，```final```、```override```与虚函数的```=0```前。

编译器不会在编译时检查```noexcept```说明。如果函数使用了```noexcept```说明的同时又可能抛出异常，则编译器不会报错（个别编译器可能会有警告）。如果这样的函数抛出了异常，则程序会调用```terminate```。上述过程对是否执行栈展开并未作约定，因此```noexcept```说明应该用在两种情况下：确定函数不会抛出异常；不知道如何处理该异常。

指定一个函数不会抛出异常可以令函数的调用者不必再考虑如何处理异常，无论函数确实不会抛出异常还是程序被终止，调用者都无需为此负责。

> Specifying that a function won’t throw effectively promises the callers of the nonthrowing function that they will never need to deal with exceptions. Either the function won’t throw, or the whole program will terminate; the caller escapes responsibility either way.

```noexcept```说明符接受一个可选的实参，该实参必须能转换为```bool```类型。如果实参为```true```，则函数（声称）不会抛出异常，否则函数可能抛出异常。

```noexcept```说明符的实参常常通过**```noexcept```运算符（```noexcept``` operator）**组合在一起。```noexcept```运算符为一元运算符，其返回值是一个```bool```类型的右值常量表达式，其表示给定的表达式是否（声称）会抛出异常。和```sizeof```类型，```noexcept```不会对运算对象求值。对于```noexcept(e)```，当且仅当```e```调用的所有函数都做了不抛出说明且```e```本身不含有```throw```时，其求值为```true```。

```c++
void f() noexcept(noexcept(g()));  // f与g的异常说明一致。
```

```noexcept```也存在类似```const```的转换。一个作了不抛出异常说明的函数指针只可以指向作了同样声明的函数；如果显式或隐式地说明函数指针可能抛出异常，则其可以指向任意（兼容的）函数。如果一个虚函数作了不抛出异常说明，则继承的虚函数也必须作出同样声明；如果虚函数未作此声明，则无此要求。

```c++
class Base {
public:
    virtual double f1(double) noexcept;
    virtual int f2() noexcept(false);
    virtual void f3();
};

class Derived : public Base {
public:
    double f1(double); // 错误。
    int f2() noexcept(false); // 正确，也可以是：noexcept(true)。
    void f3() noexcept; // 正确，也可以无noexcept。
};
```

当编译器合成拷贝控制成员时，也会为其生成异常说明。如果所有成员与基类的操作都声称不会抛出异常，则合成的成员是```noexcept```的；如果合成成员调用的任意一个函数可能抛出异常，则合成的成员是```noexcept(false)```的。如果定义了一个析构函数但是没有为其提供异常说明，则编译器将合成一个。

> When the compiler synthesizes the copy-control members, it generates an exception specification for the synthesized member. If all the corresponding operation for all the members and base classes promise not to throw, then the synthesized member is ```noexcept```. If any function invoked by the synthesized member can throw, then the synthesized member is  ```noexcept(false)```. Moreover, if we do not provide an exception specification for a destructor that we do define, the compiler synthesizes one for us. The compiler generates the same specification as it would have generated had it synthesized the destructor for that class.

## 异常类

C++标准库定义了一组**异常类（```exception``` class）**。

头文件```exception```定义了最通用的异常类```exception```，它只报告异常的发生，不提供额外的信息。其仅定义了拷贝构造函数、拷贝赋值运算符、一个虚析构函数与一个名为```what```的虚成员。

头文件```stdexcept```定义了几种通用（general-purpose）的异常类型，包括：

<table>
    <tr>
        <th>异常类</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>runtime_error</td>
        <td>只有在运行时才能检测出来的异常。</td>
    </tr>
    <tr>
        <td>range_error</td>
        <td>运行时错误：生成的结果超出了有意义的值域。</td>
    </tr>
    <tr>
        <td>overflow_error</td>
        <td>运行时错误：计算上溢。</td>
    </tr>
    <tr>
        <td>underflow_error</td>
        <td>运行时错误：计算下溢。</td>
    </tr>
    <tr>
        <td>logic_error</td>
        <td>程序逻辑错误。</td>
    </tr>
    <tr>
        <td>domain_error</td>
        <td>逻辑错误：实参对应的结果值不存在（argument for which no result exists）。</td>
    </tr>
    <tr>
        <td>invalid_argument</td>
        <td>逻辑错误：不合适（inappropriate）的实参。</td>
    </tr>
    <tr>
        <td>length_error</td>
        <td>逻辑错误：试图创建一个超出该类型最大长度的对象。</td>
    </tr>
    <tr>
        <td>out_of_range</td>
        <td>逻辑错误：使用一个超出有效范围的值。</td>
    </tr>
</table>

头文件```type_info```中定义了```bad_cast```异常类型。

其中，```bad_cast```、```runtime_error```、```logic_error```与```bad_alloc```继承自```exception```；```overflow_error```、```underflow_error```与```range_error```继承自```runtime_error```；```domain_error```、```invalid_argument```、```out_of_range```与```length_error```继承自```logic_error```。

类```exception```、```bad_cast```与```bad_alloc```定义了默认构造函数；类```runtime_error```与```logic_error```没有定义默认构造函数。只能以默认方式初始化```exception```、```bad_alloc```和```bad_cast```对象，不允许为这些对象提供初始值。其他异常类型则恰好相反：需要使用```string```或C风格字符串来初始化这些对象，不能默认初始化。初始值旨在给出关于错误的额外信息。

异常类型只定义了一个名为```what```的成员虚函数，该函数没有任何参数，返回值为一个指向C风格字符串的```const char*```，提供关于异常的一些文本信息。```what```的返回值的内容与异常对象的类型有关，如果异常类型有一个字符串初始值，则```what```返回该字符串，否则```what```返回的内容由编译器决定。

```c++
// 为Sales_data设计异常类并使用之。

class out_of_stock: public runtime_error {
public:
    explicit out_of_stock(const string &s): runtime_error(s) {}
};

class isbn_mismatch: public logic_error {
public:
    explicit isbn_mismatch(const string &s): logic_error(s) {}
    isbn_mismatch(const string &s, const string &lhs, const string &rhs): logic_error(s), left(lhs), right(rhs) {}
    const string left, right;
};

Sales_data &Sales_data::operator+=(const Sales_data &rhs) {
    if (isbn() != rhs.isbn()) throw isbn_mismatch("wrong isbns", isbn(), rhs.isbn());
    units_sold += rhs.units_sold; revenue += rhs.revenue; return *this;
}

Sales_data item1, item2, sum;
while (cin >> item1 >> item2) {
    try {
        sum = item1 + item2;
    } catch (const isbn_mismatch &e) { cerr << e.what() << ": left isbn(" << e.left << ") right isbn(" << e.right << ")" << endl; 
}
```

# 命名空间

多个库将名字放在全局空间中容易引发**命名空间污染（namespace pollution）**。传统的解决方法是将全局空间名字设置得很长来解决这一问题，这些名字通常包含表示名字所属库的前缀。

**命名空间（namespace）**为防止名字冲突提供了更加可控的机制。命名空间分割了全局命名空间，每个命名空间是一个作用域。

命名空间的定义如下：

```c++
namespace name {
    statements
}
```

其中```name```表示命名空间名称，```statements```为一系列声明或定义（不是任意语句）。只要能出现在全局作用域中的声明就能置于命名空间内，包括类、变量（及其初始化）、函数（及其定义）、模板与其他命名空间（即嵌套的命名空间）等。

```c++
namespace cplusplus_primer {
    class Sales_data {
        /* ... */
    };
    Sales_data operator+(const Sales_data&, const Sales_data&);
    class Query {
        /* ... */
    };
    class Query_base {
        /* ... */
    };
}
```

每个命名空间都是一个作用域，不同命名空间内可以有相同名字的成员。定义在某个命名空间中的名字可以直接被该命名空间内的其他成员访问，包括这些成员的内嵌作用域。位于该命名空间外的代码必须使用命名空间作用域运算符明确指出引用的名字属于哪个命名空间，位于该命名空间内的代码也可以使用这种形式。这与类定义时引用类内成员的方法类似。嵌套的命名空间可以看做命名空间的特殊成员，同样使用上述规则。

> Names defined in a namespace may be accessed directly by other members of the namespace, including scopes nested within those members.

```c++
cplusplus_primer::Query q = cplusplus_primer::Query("hello");
```

命名空间的成员可以定义在命名空间所属的（直接的或间接的）外层空间中，此时要使用命名空间作用域运算符指出成员所属的命名空间，这与类定义时定义类内成员的方法类似。不允许在不相关的作用域（包括命名空间的嵌套命名空间）内定义命名空间成员。

模板特例化必须定义在原始模板所属的命名空间内。类似地，只要在命名空间内声明了特例化，则可以在命名空间外定义之。

```c++
namespace std {
    template <> struct hash<Sales_data>;
}

template <> struct hash<Sales_data> {
    size_t operator() (const Sales_data &s) const {
        return hash<string>()(s.bookNo) ^ hash<unsigned>()(s.units_sold) ^ hash<double>()(s.revenue);
    }
};
```

## 不连续的命名空间

命名空间可以是不连续的，可以定义为几个不同的部分。当程序遇到一个命名空间时，如果之前没有同名的命名空间定义，则创建一个新的命名空间，否则打开已存在的命名空间并添加一些新的声明语句。

这一特性使得我们可以将几个分离的结构与实现文件组成一个命名空间。因此，可以按照如下方式组织命名空间：

- 类定义、函数与对象声明等属于类接口的命名空间成员可以放置在头文件中，这些头文件可以被使用这些成员的文件所```include```。
- 命名空间成员的定义则置于另外的源文件中。

因为程序中的某些实体只能被定义一次，因此这种接口与实现分离的机制保证了我们所需的函数与其他名字只被定义一次，且只要用到这些实体的地方都能看到对于该实体的声明。

定义多个不相关类型的命名空间应该使用不同的文件分别表示每个类型或每个关联类型集合。

```c++
// 头文件Sales_data.h。
// 确保在打开该命名空间前使用#include，否则我们试图将命名空间std嵌套在cplusplus_primer中。
#include <string>
namespace cplusplus_primer {
    class Sales_data {
        /* ... */
    };
    Sales_data operator+(const Sales_data&, const Sales_data&);
 // Sales_data内其他函数的声明。
}
```

```c++
// 源文件Sales_data.cc。
// 确保在打开该命名空间前使用#include。
#include "Sales_data.h"
namespace cplusplus_primer {
    // Sales_data成员与重载操作的定义。
}
```

```c++
// 源文件user.cc。
#include "Sales_data.h"
int main() {
    using cplusplus_primer::Sales_data;
    Sales_data trans1, trans2;
    // ...
    return 0;
}
```

## 全局命名空间

全局作用域中定义的名字定义于**全局命名空间（global namespace）**中。全局命名空间以隐式方式声明，并且存在于所有的程序中。每个文件将其定义在全局作用域内的实体的名字隐式地添加到全局命名空间中。

全局作用域没有名字，可以通过无名的命名空间作用域运算符引用。

```c++
::member_name  // 表示全局命名空间中的一个成员。
```

## 内联命名空间

**内联命名空间（inline namespace）**中的名字可以直接被外层命名空间引用（也可以按通常方式引用）。通过在```namespace```前添加关键字```inline```定义内联命名空间。关键字```inline```出现在命名空间第一次定义的地方，后续再打开命名空间的时候可以写```inline```，也可以不写。

```c++
inline namespace FifthEd {
    
}

namespace FifthEd {
    class Query_base {
        /* ... */
    };
}
```

当应用程序的代码在两次发布间发生改变时，常常用到内联命名空间。

```c++
namesapce FourthEd {
    class Item_base {
        /* ... */
    };
    class Query_base {
        /* ... */
    };
}

namespace cplusplus_primer {
    #include "FifthEd.h"
    #include "FourthEd.h"
}

cplusplus_primer::Query_base query_base1;  // 引用命名空间FifthEd中的Query_base类。
cplusplus_primer::FourthEd::Query_base query_base2;  // 引用命名空间FourthEd中的Query_base类。
```

## 未命名的命名空间

**未命名的命名空间（unnamed namespace）**没有命名空间名。未命名的命名空间中定义的变量拥有静态生命周期：它们在第一次使用前创建，在程序结束时销毁。

一个未命名的命名空间可以在某个给定的文件内不连续，但是不能跨越多个文件。每个文件有其自己的未命名的命名空间。如果两个文件都包含未命名的命名空间，则两者不相关。如果一个头文件定义了未命名的命名空间，则该命名空间中定义的名字在每个包含该头文件的文件中对应不同的实体。

定义在未命名的命名空间中的名字只能直接使用，不能使用作用域运算符访问其中成员。

未命名的命名空间中定义的名字的作用域与该命名空间所在的作用域相同。如果未命名的命名空间定义在文件最外层作用域中，则未命名的命名空间中的名字必须与全局作用域中的名字有所区别。

```c++
int i;
namespace {
    int i;
}

i = 10;  // 二义性错误。
```

一个未命名的命名空间也能嵌套在其他命名空间中。

```c++
namespace local {
    namespace {
        int i;
    }
}

local::i = 42;  // 通过外层命名空间名来访问。
```

未命名的命名空间可以让名字拥有文件作用域（而不是全局作用域）。

## 名字引用

由于标准库函数```move```与```forward```都有一个右值引用的函数形参，因此如果应用程序又定义了同名函数，且只有一个形参，则不管该形参是什么类型的，这两个同名函数都会冲突，这导致```move```与```forward```函数的名字冲突更容易发生。同时由于```move```与```forward```执行的是非常特殊的类型操作，应用程序想专门覆盖（override）这些函数的行为的可能性很小。由于冲突更容易发生且不太可能是有意的的，因此应该使用```std::move```或```std::forward```引用标准库的```move```与```forward```函数（而不应使用```using```声明或```using```指示）。

### 命名空间别名

**命名空间别名声明（namespace alias）**形如：```namespace alias = name```。其中```name```为命名空间，可以是嵌套的命名空间；```alias```为```name```的别名。

不能在命名空间还未定义前就声明别名。

### ```using```声明

一条**```using``` 声明（```using``` declaration）**（关键字```using```紧跟命名空间限定的命名空间成员）一次引入一个命名空间的成员，程序可以直接使用该成员而不必使用命名空间限定。

```using```声明中引入的名字从```using```声明开始到```using```声明所在的作用域结束为止的范围内可见。在此过程中，外层作用域中的同名实体将被隐藏。```using```声明的名字的作用域与```using```声明语句本身的作用域一致。

一条```using```声明可以出现在全局作用域、局部作用域、命名空间作用域与类作用域中，在类作用域中，该声明只能引用（refer to）基类成员。

注意```using```引入的是名字，而非特定的函数等。一个```using```声明包括了重载函数的所有版本以确保不违反命名空间的接口。允许用户选择性地忽略重载函数中的一部分而非全部可能导致意想不到的程序行为。一个```using```声明引入的函数将与该声明语句所属作用域中已有的其他同名函数重载。如果```using```声明出现在局部作用域内，则引入的名字将隐藏外层作用域中的同名声明。如果```using```声明中已有一个函数与新引入的函数同名且不构成重载关系，则引发错误。

```c++
using NS::print(int);  // 错误。
using NS::print;
```

### ```using```指示

一条**using指示（using directive）**（```using namespace```紧跟命名空间名，该命名空间名必须已定义）一次引入一个命名空间中的所有成员，程序可以直接使用这些成员而不必使用命名空间限定。

```using```声明中引入的名字从```using```指示开始到```using```声明所在的作用域结束为止的范围内可见。

```using```指示可以出现在全局作用域、局部作用域与命名空间作用域中，但不能出现在类作用域中。

因为命名空间中可能出现一些无法出现在局部作用域中的定义，因此```using```指示可以将命名空间成员提升到同时包含该命名空间本身与```using```指示的最近作用域。

> A ```using``` directive does not declare local aliases. Rather, it has the effect of lifting the namespace members into the nearest scope that contains both the namespace itself and the ```using``` directive.

> This difference in scope between a ```using``` declaration and a ```using``` directive stems directly from how these two facilities work. In the case of a ```using``` declaration, we are simply making name directly accessible in the local scope. In contrast, a ```using``` directive makes the entire contents of a namespace available In general, a namespace might include definitions that cannot appear in a local scope. As a consequence, a ```using``` directive is treated as if it appeared in the nearest enclosing namespace scope

```c++
namespace A {
    int i, j;
}
void f() {
    using namespace A;
    cout << i * j << endl;  // 使用命名空间A中的i与j。
}
```

```c++
namespace blip {
    int i = 16, j = 15, k = 23;
    // 其他声明。
}
int j = 0;

void manip() {
    using namespace blip;
    ++i;  // blip::i。
    ++j;  // 二义性错误：blip::j或全局的j。
    ++::j;  // 全局的j。
    ++blip::j;
    int k = 97;
    ++k;  // 局部的k。
}
```

当```using```指示引入某个命名空间，如果命名空间中的某个函数与该命名空间所属作用域内的某个函数同名，则命名空间中的该函数被添加到重载集中。如果存在多个```using```指示，则来自每个命名空间中的名字都会成为候选函数集的一部分。

```c++
namespace libs_R_us {
    extern void print(int);
    extern void print(double);
}
void print(const string&);
using namespace libs_R_us;
/* 此时的候选函数包括：
 * libs_R_us::print(int)
 * libs_R_us::print(double)
 * ::print(const string&)
 */
void fooBar(int ival) {
    print("Value: ");  // 调用::print(const string&)。
    print(ival);  // 调用ibs_R_us::print(int)。
}
```

```c++
namespace AW {
    int print(int);
}
namespace Primer {
    double print(double);
}

using namespace AW;
using namespace Primer;
long double print(long double);
int main() {
    print(1);  // 调用AW::print(int)。
    print(3.1);  // 调用Primer::print(double)。
    return 0;
}
```

由于```using```指示的特点，```using```指示容易造成名字冲突（例如程序使用多个库或一个库的多个版本之间存在名字冲突）或二义性错误（并且这种二义性错误有时只有在使用时才能被检测出来），因此应尽量避免使用。

头文件如果在顶层作用域内有```using```指示或```using```声明，则会将名字注入到所有包含该头文件的文件中。通常情况下，头文件负责定义接口名字，因此头文件只能在函数或命名空间内使用```using```指示或```using```声明。

> A header that has a ```using``` directive or declaration at its top-level scope injects names into every file that includes the header. Ordinarily, headers should define only the names that are part of its interface, not names used in its own implementation. As a result, header files should not contain ```using``` directives or ```using``` declarations except inside functions or namespaces (§ 3.1, p. 83).

> One place where using directives are useful is in the implementation files of the namespace itself.

## 作用域

命名空间的名字查找遵循常规的查找规则。

```c++
namespace A {
    int i;
    namespace B {
        int i;  // 在B中隐藏了A::i。
        int j;
        int f1() {
            int j;  // 在f1中隐藏了A::B::j。
            return i;  // 返回B::i。
        }
    }  //  B中定义的名字自此不可见。
    int f2() {
        return j;  // 错误。
    }
    int j = i;  // 使用A::i初始化A::j。
}
```

```c++
namespace A {
    int i;
    int k;
    class C1 {
    public:
        C1() : i(0), j(0) {}  // 初始化C1::i与C1::j。
        int f1() {
            return k;  // 返回A::k。
        }
        int f2() {
            return h;  // 错误：h未定义。
        }
        int f3();
    private:
        int i;  // 在C1中隐藏了A::i。
        int j;
    };
    int h = i;  // 使用A::i初始化h。
}

int A::C1::f3() {
    return h;  // 返回A::h。
}
```

当给函数传递一个类类型的对象时，编译器除了执行正常的作用域查找外，还会在实参类（类类型的引用与指针也适用）定义的命名空间中查找该函数名。

```c++
string;
cin >> s;  // 等价于operator>>(cin, s)，编译器还会在cin与string类所属的命名空间中查找operator>>运算符。因此不必对operator>>使用std限定符或using声明。
```

对于接受类类型实参的函数来说，重载过程中的名字查找包括实参类以及实参类的基类所属的命名空间，该过程中所有与被调用函数同名的函数都被添加到候选集中，即使某些函数在调用点不可见。

> As we saw in the previous section, name lookup for functions that have class-type arguments includes the namespace in which each argument’s class is defined. This rule also impacts how we determine the candidate set. Each namespace that defines a class used as an argument (and those that define its base classes) is searched for candidate functions. Any functions in those namespaces that have the same name as the called function are added to the candidate set. These functions are added even though they otherwise are not visible at the point of the call:

```c++
namespace NS {
    class Quote {
        /* ... */
    };
    void display(const Quote&) {
        /* ... */
    }
}
class Bulk_item : public NS::Quote {
    /* ... */
}

int main() {
    Bulk_item book1;
    display(book1);  // 候选函数包括NS::display。
    return 0;
}
```

一个另外的未声明的类或函数如果第一次出现在友元声明中，则我们认为它是最近的外层命名空间中的成员。

> Recall that when a class declares a friend, the friend declaration does not make the friend visible (§ 7.2.1, p. 270). However, an otherwise undeclared class or function that is first named in a friend declaration is assumed to be a member of the closest enclosing namespace. The combination of this rule and argument-dependent lookup can lead to surprises:

```c++
namespace A {
    class C {
        // 这两个函数在友元声明外没有其他的声明。
        // 这些函数隐式成为命名空间A中的成员。
        friend void f2();  // 除非另有声明，否则不会被找到。
        friend void f(const C&);  // 根据实参查找规则可以找到。
    };
}

int main() {
    A::C cobj;
    f(cobj);  // 正确：通过在A::C中的友元声明找打A::f。
    f2();  // 错误：A::f2未被声明。
}
```

# 标准库设施

## 字符处理

```cctype```头文件中定义的处理字符的函数如下，其中```c```为字符：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>isalnum(c)</td>
        <td>如果c为字母或数字，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isalpha(c)</td>
        <td>如果c为字母，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>iscntrl(c)</td>
        <td>如果c为控制字符，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isdigit(c)</td>
        <td>如果c为数字，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isgraph(c)</td>
        <td>如果c不是空白（space）且可打印，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>islower(c)</td>
        <td>如果c是小写字母，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isprint(c)</td>
        <td>如果c可打印字符，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>ispunct(c)</td>
        <td>如果c为标点符号（即c不是控制字符、数组、字母或可打印空白（whitespace）），则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isspace(c)</td>
        <td>如果c为空白字符（whitespace），则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isupper(c)</td>
        <td>如果c为大写字母，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>isxdigit(c)</td>
        <td>如果c为十六进制数字，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>tolower(c)</td>
        <td>如果c为大写字母，则返回对应的小写字母，否则返回未改变的c。</td>
    </tr>
    <tr>
        <td>toupper(c)</td>
        <td>如果c为小写字母，则返回对应的大写字母，否则返回未改变的c。</td>
    </tr>
</table>

```c++
// 统计string中标点符号出现次数。
string s("Hello World!!!");
decltype(s.size()) punct_cnt = 0;
for (auto c : s) {
    if (ispunct(c)) {
        ++punct_cnt;
    }
}
cout << punct_cnt << " punctuation characters in " << s << endl;
```


## ```pair```

```pair```表示元素对，定义在头文件```utility```中，其支持如下操作，其中```p```、```p1```与```p2```为```pair```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>pair&lt;T1, T2&gt; p</td>
        <td>p的两个成员的类型分别为T1与T2，都进行了值初始化。</td>
    </tr>
    <tr>
        <td>pair&lt;T1, T2&gt; p(v1, v2)</td>
        <td rowspan="3">p的两个成员的类型分别为T1与T2，分别使用v1与v2初始化。</td>
    </tr>
    <tr>
        <td>pair&lt;T1, T2&gt; p = {v1, v2}</td>
    </tr>
    <tr>
        <td>pair&lt;T1, T2&gt; p {v1, v2}</td>
    </tr>
    <tr>
        <td>make_pair(v1, v2)</td>
        <td>返回一个由v1与v2初始化的pair，pair的类型由v1与v2类型推断而来。</td>
    </tr>
    <tr>
        <td>p.first</td>
        <td>p的名为first的public数据成员。</td>
    </tr>
    <tr>
        <td>p.second</td>
        <td>p的名为second的public数据成员。</td>
    </tr>
    <tr>
        <td>p1 relop p2</td>
        <td>relop为&lt;、&lt;=、&gt;或&gt;=，返回p1与p2的字典序比较结果，使用&lt;比较元素。</td>
    </tr>
    <tr>
        <td>p1 == p2</td>
        <td rowspan="2">如果p1与p2的first与second成员分别相等，则p1 == p2，否则p1 != p2。</td>
    </tr>
    <tr>
        <td>p1 != p2</td>
    </tr>
</table>

```c++
pair<string, int> process(vector<string> &v) {
    // ...
    if (!v.empty()) {
        return {v.back(), v.back().size()};  // 列表初始化。
    }
    else {
        return pair<string, int>();  // 显式构造返回值。
    }
}
```

## ```initializer_list```

```initializer_list```定义在```initializer_list```头文件中，表示某种特定类型值的数组，其支持如下操作，其中```lst```、```lst2```为```initializer_list```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>initializer_list&lt;T&gt; lst</td>
        <td>元素类型为T的空列表。</td>
    </tr>
    <tr>
        <td>initializer_list&lt;T&gt; lst{a, b, c...}</td>
        <td>lst中的元素是列表中的元素的拷贝，lst中的元素数量等于列表中的元素数量。</td>
    </tr>
    <tr>
        <td>lst2(lst)</td>
        <td rowspan="2">令lst2共享lst中的元素。</td>
    </tr>
    <tr>
        <td>lst2 = lst</td>
    </tr>
    <tr>
        <td>lst.size()</td>
        <td>lst中的元素数量。</td>
    </tr>
    <tr>
        <td>lst.begin()</td>
        <td>lst的首元素迭代器。</td>
    </tr>
    <tr>
        <td>lst.end()</td>
        <td>lst的尾后迭代器。</td>
    </tr>
</table>

```initializer_list```中的元素永远是```const```的。

```c++
// 打印错误信息。
// 将initializer_list当作可变数量实参。
void error_msg(initializer_list<string> il) {
    for (auto beg = il.begin(); beg != il.end(); ++beg) {
        cout << *beg << " " ;
        cout << endl;
    }
}

// 将值的序列传递给initializer_list形参，必须将序列放在花括号中。
if (expected != actual) {
    error_msg({"functionX", expected, actual});
}
else {
    error_msg({"functionX", "okay"});
}
```

```c++
// 含有initializer_list形参的函数也可以有其他形参。
// ErrCode：表示错误类型。
void error_msg(ErrCode e, initializer_list<string> il) {
    cout << e.msg() << ": ";
    for (const auto &elem : il) {
        cout << elem << " " ;
        cout << endl;
    }
}

// 需要额外传递一个ErrCode实参。
if (expected != actual) {
    error_msg(ErrCode(42), {"functionX", expected, actual});
}
else {
    error_msg(ErrCode(0), {"functionX", "okay"});
}
```

## ```tuple```

```tuple```表示元组，其支持的操作如下：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>tuple&lt;T1, T2, ..., Tn&gt; t</td>
        <td>定义一个有n个成员，且成员类型分别为T1、T2、……、Tn的tuple，所有成员被值初始化。</td>
    </tr>
    <tr>
        <td>tuple&lt;T1, T2, ..., Tn&gt; t(v1, v2, ..., vn)</td>
        <td>定义一个有n个成员，且成员类型分别为T1、T2、……、Tn的tuple，每个成员分别使用v1、v2、……、vn初始化。</td>
    </tr>
    <tr>
        <td>make_tuple(v1, v2, ..., vn)</td>
        <td>返回一个使用给定初始值初始化的tuple，tuple的类型从初始值推断而来。</td>
    </tr>
    <tr>
        <td>t1 == t2</td>
        <td rowspan="2">返回tuple t1与t2的字典序比较结果，使用==比较元素，t1与t2的成员数量必须相同，且对应成员必须可以使用==进行比较。</td>
    </tr>
    <tr>
        <td>t1 != t2</td>
    </tr>
    <tr>
        <td>t1 relop t2</td>
        <td>relop为&lt;、&lt;=、&gt;或&gt;=，返回tuple t1与t2的字典序比较结果，使用&lt;比较元素。t1与t2的成员数量必须相同，且对应成员必须可以使用&lt;进行比较。</td>
    </tr>
    <tr>
        <td>get&lt;i&gt;(t)</td>
        <td>返回tuple t的第i个成员的引用（从0计数）。如果t为左值，结果为左值引用，否则结果为右值引用。tuple的所有成员都是public的。</td>
    </tr>
    <tr>
        <td>tuple_size&lt;tupleType&gt;::value</td>
        <td>一个类模板的一个名为value的public constexpr static数据成员，表示tupleType类型的tuple中的成员的数量。</td>
    </tr>
    <tr>
        <td>tuple_element&lt;i, tupleType&gt;::type</td>
        <td>一个类模板的一个名为type的public成员，表示tupleType类型的tuple中的第i个成员的类型。i为整型常量。</td>
    </tr>
</table>
```tuple```类型及其伴随类型与函数均定义在```tuple```头文件中。

```tuple```的一个常见用途是从一个函数返回多个值。

```c++
typedef tuple<vector<Sales_data>::size_type, vector<Sales_data>::const_iterator, vector<Sales_data>::const_iterator> matches;

// 从书店中找到匹配书籍记录。
// files保存多家书店，每家书店保存多本书。
// book为待查找的书籍。
vector<matches> findBook(const vector<vector<Sales_data>> &files, const string &book) {
    vector<matches> ret;
    for (auto it = files.cbegin(); it != files.cend(); ++it) {
        auto found = equal_range(it -> cbegin(), it -> cend(), book, compareIsbn);
        if (found.first != found.second) {
             ret.push_back(make_tuple(it - files.cbegin(), found.first, found.second));
        }
    }
    return ret;
}

// 打印结果。
void reportResults(istream &in, ostream &os, const vector<vector<Sales_data>> &files) {
    string s;
    while (in >> s) {
        auto trans = findBook(files, s);
        if (trans.empty()) {
            cout << s << " not found in any stores" << endl;
            continue;
        }
        for (const auto &store : trans) {
             os << "store " << get<0>(store) << " sales: " << accumulate(get<1>(store), get<2>(store), Sales_data(s)) << endl; 
        }
    }
}

// compareIsbn的定义如下：
bool compareIsbn(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.isbn() < rhs.isbn();
}
```

## ```bitset```

```bitset```表示位集合，定义在bitset头文件中。在```bitset```中，位置为0的位为低（low-order）位，位置最大的位为高（high-order）位。

```bitset```的定义与初始化方式如下：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>bitset&lt;n&gt; b</td>
        <td>定义一个有n位、每位为0的bitset。该构造函数是constexpr的。</td>
    </tr>
    <tr>
        <td>bitset&lt;n&gt; b(u)</td>
        <td>定义一个bitset，包含unsigned long long u的低n位的拷贝。如果n大于unsigned long long的大小，则b中超出的高位被置0。该构造函数是constexpr的。</td>
    </tr>
    <tr>
        <td>bitset&lt;n&gt; b(s, pos, m, zero, one)</td>
        <td>定义一个bitset，包含string s从位置pos开始的min(m, s.size() - pos)个字符的拷贝。s只能包含字符zero与one，否则将抛出invalid_argument异常。这些字符在b中分别被保存为zero与one。pos默认为0，m默认为string::npos，zero默认为'0',one默认为'1'。</td>
    </tr>
    <tr>
        <td>bitset&lt;n&gt; b(cp, pos, m, zero, one)</td>
        <td>与上一个构造函数类似，但是使用cp指向的字符数组替代了string s。如果未提供m，则cp必须指向一个C风格字符串；如果提供了m，则从cp开始必须至少有m个zero或one字符。</td>
    </tr>
</table>
其中，后两个构造函数是```explicit```的，且这两个构造函数中，字符直接表示位模式，字符串中下标最小的字符对应高位，反之亦然。

```bitset```支持的操作如下，其中```b```为```bitset```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>b.any()</td>
        <td>若b中有置位的位，则返回true，否则返回false</td>
    </tr>
    <tr>
        <td>b.all()</td>
        <td>若b中所有位均置位，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>b.none()</td>
        <td>若b中没有置位的位，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>b.count()</td>
        <td>返回b中置位的位的数量。</td>
    </tr>
    <tr>
        <td>b.size()</td>
        <td>一个constexpr函数，返回b的位数。</td>
    </tr>
    <tr>
        <td>b.test(pos)</td>
        <td>如果b中位置pos（从0计数）处的位置位，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>b.set(pos, v)</td>
        <td>将b中位置pos处的位设置为bool v。v默认为true。</td>
    </tr>
    <tr>
        <td>b.set()</td>
        <td>置位b中的所有位。</td>
    </tr>
    <tr>
        <td>b.reset(pos)</td>
        <td>将b中位置pos处的位复位。</td>
    </tr>
    <tr>
        <td>b.reset()</td>
        <td>复位b中的所有位。</td>
    </tr>
    <tr>
        <td>b.flip(pos)</td>
        <td>改变b中位置pos处的位的状态。</td>
    </tr>
    <tr>
        <td>b.flip()</td>
        <td>改变b中所有位的状态。</td>
    </tr>
    <tr>
        <td>b[pos]</td>
        <td>返回b中位置pos处的位。如果b是const的，则该位置位时返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>b.to_ulong()</td>
        <td rowspan="2">返回一个unsigned long或一个unsigned long long的值，其位与b相同。如果b中的位模式不能放入指定的结果类型，则抛出overflow_error异常。</td>
    </tr>
    <tr>
        <td>b.to_ullong</td>
    </tr>
    <tr>
        <td>b.to_string(zero, one)</td>
        <td>返回一个string表示b中的位模式。字符zero与one表示b中的0与1，zero默认为'0'，one默认为'1'。</td>
    </tr>
    <tr>
        <td>os &lt;&lt; b</td>
        <td>将b中的位打印为字符0或1，打印到流os中。</td>
    </tr>
    <tr>
        <td>os &gt;&gt; b</td>
        <td>从流is读取字符存入b。当下一个字符不是0或1，或已经读入b.size()个位时，读取过程停止。</td>
    </tr>
</table>

```c++
// 使用位运算符表示测验结果，假设一共30个学生。

bool status;
unsigned long quizA = 0;
quizA |= 1UL << 27;  // 表示学生No.27通过了测验。
status = quizA & (1UL << 27);  // 查看学生No.27是否通过测验。
quizA &= ~(1UL << 27);  // 表示学生No.27没有通过测验。

// 使用bitset表示测验结果，假设一共30个学生。
bitset<30> quizB;
quizB.set(27);  // 表示学生No.27通过了测验。
status = quizB[27];  // 查看学生No.27是否通过测验。
quizB.reset(27);  // 表示学生No.27没有通过测验。
```

## 正则表达式

**正则表达式（regular expression）**为一种描述字符序列的方法。

### ```regex```

```regex```是表示正则表达式的类，其支持的操作如下，其中```r```与```r1```为正则表达式：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
	<tr>
        <td>regex r(re)</td>
        <td rowspan="2">re为一个string、表示字符范围的迭代器对、一个指向空字符结尾的字符数组的指针、一个字符指针和一个计数器或一个花括号包围的字符列表，表示一个正则表达式。f为标志，默认为ECMAScript。</td>
    </tr>
    <tr>
        <td>regex r(re, f)</td>
    </tr>
    <tr>
        <td>r1 = re</td>
        <td rowspan="2">将r1中的正则表达式替换为正则表达式re。re可以是另一个regex对象、一个string、一个指向空字符结尾的字符数组的指针或一个花括号包围的字符列表。f为标志，默认为ECMAScript。</td>
    </tr>
    <tr>
        <td>r1.assign(re, f)</td>
    </tr>
    <tr>
        <td>r.mark_count()</td>
        <td>返回r中子表达式的数量。</td>
    </tr>
    <tr>
        <td>r.flags()</td>
        <td>返回r的标志集。</td>
    </tr>
</table>

其中构造函数与赋值操作可能抛出类型为```regex_error```的异常。

其中定义```regex```时可指定的标志如下，这些标志表示```regex```对象如何执行：

<table>
    <tr>
        <th>标志</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>icase</td>
        <td>匹配过程中忽略大小写。</td>
    </tr>
    <tr>
        <td>nosubs</td>
        <td>不保存匹配的子表达式。</td>
    </tr>
    <tr>
        <td>optimize</td>
        <td>执行速度优先于构造速度。</td>
    </tr>
    <tr>
        <td>ECMAScript</td>
        <td>使用ECMA-262指定的语法。</td>
    </tr>
    <tr>
        <td>basic</td>
        <td>使用POSIX基本的正则表达式语法。</td>
    </tr>
    <tr>
        <td>extended</td>
        <td>使用POSIX扩展的正则表达式语法。</td>
    </tr>
    <tr>
        <td>awk</td>
        <td>使用POSIX版本的awk语言的语法。</td>
    </tr>
    <tr>
        <td>grep</td>
        <td>使用POSIX版本的grep的语法。</td>
    </tr>
    <tr>
        <td>egrep</td>
        <td>使用POSIX版本的egrep的语法。</td>
    </tr>
</table>

```c++
// 查找违反模式的（第一个）单词。
string pattern("[^c]ei");
pattern = "[[:alpha:]]*" + pattern + "[[:alpha:]]*";
regex r(pattern);
smatch results;
string test_str = "receipt freind theif receive";
if (regex_search(test_str, results, r)) {
    cout << results.str() << endl;
}
```

```c++
// 匹配C++文件名。
regex r("[[:alnum:]]+\\.(cpp|cxx|cc)$", regex::icase);  // 用于匹配C++文件名的正则表达式。
smatch results;
string filename;
while (cin >> filename) {
    if (regex_search(filename, results, r)) {
        cout << results.str() << endl;
    }
}
```

```regex```中也有特殊字符，例如，```.```匹配任意字符。此时需要使用```\```来转义。但是```\```也是C++中的特殊字符，因此需要使用```\\```来表示普通的反斜线，因此要想在```regex```中使用普通字符点```.```，则需要写成```\\.```，同样地，普通字符```\```需要写成```\\\\```。

### 异常

正则表达式本身可以视作一种“语言（program）”，这种语言不是C++编译器解释的，而是当运行时、```regex```对象被初始化或赋值时才被“编译的”。因此正则表达式也可能存在错误。

如果正则表达式存在错误，则会抛出```regex_error```异常。类似标准异常类型，```regex_error```有一个名为```what```的函数来描述发生了什么错误。```regex_error```还有一个名为```code```的成员，用来返回错误类型对应的数值编码，该值由具体实现定义。

```regex```错误类型如下：

<table>
    <tr>
        <th>标志</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>error_collate</td>
        <td>非法的元素校对（collating element）请求。</td>
    </tr>
    <tr>
        <td>error_ctype</td>
        <td>非法的字符类。</td>
    </tr>
    <tr>
        <td>error_escape</td>
        <td>非法的转义字符或尾置转义。</td>
    </tr>
    <tr>
        <td>error_backref</td>
        <td>非法的向后引用。</td>
    </tr>
    <tr>
        <td>error_brack</td>
        <td>不匹配的方括号。</td>
    </tr>
    <tr>
        <td>error_paren</td>
        <td>不匹配的圆括号。</td>
    </tr>
    <tr>
        <td>error_brace</td>
        <td>不匹配的花括号。</td>
    </tr>
    <tr>
        <td>error_badbrace</td>
        <td>{}中的范围非法。</td>
    </tr>
    <tr>
        <td>error_range</td>
        <td>非法的字符范围（如[z-a]）。</td>
    </tr>
    <tr>
        <td>error_space</td>
        <td>内存不足，无法处理此正则表达式。</td>
    </tr>
    <tr>
        <td>error_badrepeat</td>
        <td>重复字符（*、?、+或{）前没有合法的正则表达式。</td>
    </tr>
    <tr>
        <td>error_complexity</td>
        <td>要求的匹配过于复杂。</td>
    </tr>
    <tr>
        <td>error_stack</td>
        <td>内存不足，无法处理匹配。</td>
    </tr>
</table>
这些错误类型定义在```regex```（头文件）与```regex_constants::error_type```中。


```c++
try {
    regex r("[[:alnum:]+\\.(cpp|cxx|cc)$", regex::icase);  // 漏掉有括号，构造函数抛出异常。
} catch (regex_error e) {
    cout << e.what() << "\ncode: " << e.code() << endl;
}
```

因为正则表达式在运行时“编译”，因此正则表达式的编译是很慢的，尤其是使用了扩展的正则表达式语法或复杂的正则表达式时。因此构造一个```regex```对象或为其赋值可能是非常耗时的，要避免创建不必要的正则表达式。特别地，如果要在一个循环中使用正则表达式，应该在循环外创建它，而不是在每次迭代时都编译它。

### 匹配

```regex_match```与```regex_search```函数用于确定字符序列与给定的```regex```是否匹配。两个函数均返回```bool```值。如果整个输入序列与```regex```匹配，则```regex_match```返回```true```；如果输入序列的某个子串与```regex```匹配，则```regex_search```返回```true```。```regex_match```与```regex_search```的参数模式如下：

<table>
    <tr>
        <th>参数模式</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>(seq, m, r, mft)</td>
        <td rowspan="2">在字符序列seq中查找regex r中的正则表达式。seq为一个string、表示字符范围的迭代器对或一个指向空字符结尾的字符数组的指针；m为一个match对象，用来保存匹配的细节，m与seq必须类型兼容；mft为可选的regex_constants::match_flag_type值，会影响匹配过程。</td>
    </tr>
     <tr>
        <td>(seq, r, mft)</td>
    </tr>
</table>

### 迭代器

我们可以使用```sregex_iterator```来获得与```regex```的所有匹配。```sregex_iterator```为迭代器适配器，绑定到一个输入序列和一个```regex```对象上，其支持的操作如下，其中```it```、```it1```与```it2```为```sregex_iterator```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>sregex_iterator it(b, e, r)</td>
        <td>一个sregex_iterator，用于遍历迭代器b与e表示的string，其调用sregex_search(b, e, r)将it定位到输入中第一个匹配的位置，匹配由regex r决定。</td>
    </tr>
    <tr>
        <td>sregex_iterator end</td>
        <td>sregex_iterator的尾后迭代器。</td>
    </tr>
    <tr>
        <td>*it</td>
        <td rowspan="2">根据最后一个调用regex_search的结果，返回一个指向smatch的引用或指针。</td>
    </tr>
    <tr>
        <td>it -&gt;</td>
    </tr>
    <tr>
        <td>++it</td>
        <td rowspan="2">从输入序列当前匹配位置后（start just after the current match）的位置调用regex_search，前置版本返回递增后的迭代器的引用，后置版本返回旧值。</td>
    </tr>
    <tr>
        <td>it++</td>
    </tr>
    <tr>
        <td>it1 == it2</td>
        <td rowspan="2">如果两个sregex_iterator都是尾后迭代器，或两个非尾后迭代器是从相同的输入序列和regex对象构造的，则两者相等，否则不相等。</td>
    </tr>
    <tr>
        <td>it1 != it2</td>
    </tr>
</table>

```c++
// 查找违反模式的（第一个）单词，使用迭代器搜索所有匹配单词。
// file保存了要搜索的输入文件的所有内容。
string pattern("[^c]ei");
pattern = "[[:alpha:]]*" + pattern + "[[:alpha:]]*";
regex r(pattern, regex::icase);
for (sregex_iterator it(file.begin(), file.end(), r), end_it; it != end_it; ++it) {  // file为一个字符序列。
    cout << it -> str() << endl;
}
```

### 子表达式

一个正则表达式模式中通常包含一个或多个**子表达式（subexpression）**，子表达式是模式的一部分。正则表达式通常用括号表示子表达式。

```c++
// 重写匹配C++文件名，只打印文件名的主体名称：
regex r("([[:alnum:]]+)\\.(cpp|cxx|cc)$", regex::icase);  // 用于匹配C++文件名的正则表达式，包含两个子表达式，分别表示主体名称与后缀名。
smatch results;
string filename;
while (cin >> filename) {
    if (regex_search(filename, results, r)) {
        cout << results.str(1) << endl;
    }
}
```

子表达式的一个常见用于是验证匹配特定格式的数据。

```c++
// 检查（美国）电话号码格式是否正确：

/* （美国）电话号码格式：区号 + 可选的分隔符 + 7位本地号码。
 * 区号：可选的左括号 + 区号 + 可选的右括号
 * 本地号码：号码的前3位数字 + 可选的分隔符 + 号码的后4位数字。
 */
string phone = "(\\()?(\\d{3})(\\))?([-. ])?(\\d{3})([-. ]?)(\\d{4})";

regex r(phone);
smatch m;
string s;
while (getline(cin, s)) {
    for (sregex_iterator it(s.begin(), s.end(), r), end_it; it != end_it; ++it) {
        if (valid(*it)) {
             cout << "valid: " << it->str() << endl;
        } else {
            cout << "not valid: " << it->str() << endl;
        }
    }
}

bool valid(const smatch &m) {
    if(m[1].matched) {
        return m[3].matched && (m[4].matched == 0 || m[4].str() == " ");
    } else {
        return !m[3].matched && m[4].str() == m[6].str();
    }
}
```

### 替换

在输入序列中查找并替换正则表达式的操作如下：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>m.format(dest, fmt, mft)</td>
        <td rowspan="2">使用格式字符串fmt、match（smatch或ssub_match） m与可选的match_flag_type mft生成格式化输出。第一个版本写入迭代器dest指向的目的位置，此时fmt可以为一个string或表示字符范围的指针对；第二个版本返回string，其中fmt可以为一个string或指向空字符结尾的字符数组的指针。mft默认为format_default。</td>
    </tr>
    <tr>
        <td>m.format(fmt, mft)</td>
    </tr>
    <tr>
        <td>regex_replace(dest, seq, r, fmt, mft)</td>
        <td rowspan="2">遍历seq，使用regex_search去查找与regex r匹配的子串，并使用格式字符串fmt与可选的match_flag_type mft来生成输出。第一个版本将输出写入到迭代器dest指定的位置，此时seq可以为迭表示字符范围的迭代器对；第二个版本返回一个string表示输出，此时seq可以为一个string或一个指向空字符结尾的字符数组的指针。fmt可以是一个string、指向空字符结尾的字符数组的指针。mft的默认为format_default。</td>
    </tr>
    <tr>
        <td>regex_replace(seq, r, fmt, mft)</td>
    </tr>
</table>
```c++
// 将号码格式改为ddd.ddd.dddd。
string fmt = "$2.$5.$7";  // $后跟子表达式的索引表示特定的子表达式。
regex r(phone);
string number = "(908) 555-1800";
cout << regex_replace(number, r, fmt) << endl;
```

```c++
/* 替换文件中的电话号码
 * 如将：
 *     morgan (201) 555-2368 862-555-0123
 *     drew (973)555.0130
 *     lee (609) 555-0132 2015550175 800.555-0000
 * 替换为：
 *     morgan 201.555.2368 862.555.0123
 *     drew 973.555.0130
 *     lee 609.555.0132 201.555.0175 800.555.0000
 */
string phone = "(\\()?(\\d{3})(\\))?([-. ])?(\\d{3})([-. ])?(\\d{4})";
regex r(phone);
smatch m;
string s;
string fmt = "$2.$5.$7";
while (getline(cin, s)) {
    cout << regex_replace(s, r, fmt) << endl;
}
```

### 匹配结果

```smatch```为容器类，保存在```string```中搜索的结果。而```ssub_match```则保存在```string```中匹配的子表达式的结果。

```smatch```与```ssub_match```支持的操作如下，其中```m```为```smatch```或```ssub_match```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>m.ready()</td>
        <td>如果已经通过regex_serach或regex_match设置了m，则返回true，否则返回false。如果其返回false，则对m进行操作是未定义的。</td>
    </tr>
    <tr>
        <td>m.size()</td>
        <td>如果匹配失败，则返回0，否则返回最近一次匹配的正则表达式中的子表达式的数量加1。</td>
    </tr>
    <tr>
        <td>m.empty()</td>
        <td>如果m.size()为0，则返回true，否则返回false。</td>
    </tr>
    <tr>
        <td>m.prefix()</td>
        <td>一个ssub_match，表示匹配前的序列。</td>
    </tr>
    <tr>
        <td>m.suffix()</td>
        <td>一个ssub_match，表示匹配尾后的序列。</td>
    </tr>
     <tr>
        <td>m.format(...)</td>
        <td>见替换部分。</td>
    </tr>
    <tr>
        <td>m.length(n)</td>
        <td>第n个匹配的子表达式的大小。n默认为0，且必须小于m.size()，第1个子匹配（索引为0）表示整个匹配，下同。</td>
    </tr>
    <tr>
        <td>m.position(n)</td>
        <td>第n个子表达式距离序列开始的距离。</td>
    </tr>
    <tr>
        <td>m.str(n)</td>
        <td>第n个子表达式匹配的string。</td>
    </tr>
    <tr>
        <td>m[n]</td>
        <td>一个ssub_match对象，表示第n个子表达式。</td>
    </tr>
    <tr>
        <td>m.begin()</td>
        <td rowspan="4">表示m中sub_match元素范围的迭代器。与往常一样，cbegin与cend函数返回const_iterator。</td>
    </tr>
    <tr>
        <td>m.end()</td>
    </tr>
    <tr>
        <td>m.cbegin()</td>
    </tr>
    <tr>
        <td>m.cend()</td>
    </tr>
</table>
```c++
// 查找违反模式的（第一个）单词，使用迭代器搜索所有匹配单词。
// 打印匹配的上下文。
for (sregex_iterator it(file.begin(), file.end(), r), end_it; it != end_it; ++it) { // file为一个字符序列。
    auto pos = it -> prefix().length();
    pos = pos > 40 ? pos - 40 : 0;
    cout << it->prefix().str().substr(pos) << "\n\t\t>>> " << it->str() << " <<<\n" << it->suffix().str().substr(0, 40) << endl;
}
```

子匹配额外支持的操作如下，这些操作适用于```ssub_match```：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>matched</td>
        <td>一个public bool数据成员，指出此ssub_match是否已匹配。</td>
    </tr>
    <tr>
        <td>first</td>
        <td rowspan="2">public数据成员，指向匹配序列首元素和尾后位置的迭代器。如果没有匹配，则first与second相等。</td>
    </tr>
    <tr>
        <td>second</td>
    </tr>
    <tr>
        <td>length()</td>
        <td>返回匹配长度。如果match为false，则返回0。</td>
    </tr>
    <tr>
        <td>str()</td>
        <td>返回一个包含输入中匹配部分的string。如果matched为false，则返回空string。</td>
    </tr>
    <tr>
        <td>s = ssub</td>
        <td>将ssub_match ssub转换为string s，等价于s = ssub.str()。转换运算符不是explicit的。</td>
    </tr>
</table>
### 输入序列类型

针对不同类型的输入序列，要使用不同的输入序列。例如```regex```保存类型为```char```的正则表达式，标准库定义了一个```wregex```保存类型为```wchar_t```，其操作与```regex```完全相同，唯一的区别就是```wregex```的初始值必须使用```wregex```。对于不同的输入序列，必须使用与之匹配的正则表达式库。

以下是针对不同的输入序列应该使用的正则表达式类。

<table>
    <tr>
        <th>输入序列类型</th>
        <th>使用的正则表达式类</th>
    </tr>
    <tr>
        <td>string</td>
        <td>regex、smatch、ssub_match、sregex_iterator</td>
    </tr>
    <tr>
        <td>const char*</td>
        <td>regex、cmatch、csub_match、cregex_iterator</td>
    </tr>
    <tr>
        <td>wstring</td>
        <td>wregex、wsmatch、wssub_match、wsregex_iterator</td>
    </tr>
    <tr>
        <td>const wchar_t*</td>
        <td>wregex、wcmatch、wcsub_match、wcregex_iterator</td>
    </tr>
</table>

```c++
regex r("[[:alnum:]]+\\.(cpp|cxx|cc)$", regex::icase);

// 错误的使用。
smatch results;
if (regex_search("myfile.cc", results, r)) {  // 错误，输入为char*。
    cout << results.str() << endl;
}

// 正确的使用。
cmatch results;
if (regex_search("myfile.cc", results, r)) {
    cout << results.str() << endl;
}
```

### 匹配标志

标准库定义了在替换过程中控制匹配过程或格式化的标志。这些标志可以传递给函数```regex_search```或```regex_match```，或类```smatch```的```format```成员。

这些匹配标志的类型为```match_flag_type```，且位于```std::regex_constant```命名空间中。这些标志如下：

<table>
    <tr>
        <th>匹配标志</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>match_default</td>
        <td>等价于format_default。</td>
    </tr>
    <tr>
        <td>match_not_bol</td>
        <td>不将首字符作为行首处理。</td>
    </tr>
    <tr>
        <td>match_not_eol</td>
        <td>不将尾字符作为行尾处理。</td>
    </tr>
    <tr>
        <td>match_not_bow</td>
        <td>不将首字符作为单词首处理。</td>
    </tr>
    <tr>
        <td>match_not_eow</td>
        <td>不将尾字符作为单词尾处理。</td>
    </tr>
    <tr>
        <td>match_any</td>
        <td>如果存在多个匹配，则可返回任意一个匹配。</td>
    </tr>
    <tr>
        <td>match_not_null</td>
        <td>不匹配任何空序列。</td>
    </tr>
    <tr>
        <td>match_continuous</td>
        <td>匹配必须从输入的首字符开始。</td>
    </tr>
    <tr>
        <td>match_prev_avail</td>
        <td>输入序列包含第一个匹配之前的内容。</td>
    </tr>
    <tr>
        <td>format_default</td>
        <td>使用ECMAScript规则替换字符串。</td>
    </tr>
    <tr>
        <td>format_sed</td>
        <td>使用POSIX sed规则替换字符串。</td>
    </tr>
    <tr>
        <td>format_no_copy</td>
        <td>不输出输入序列中未匹配的部分。</td>
    </tr>
    <tr>
        <td>format_first_only</td>
        <td>只替换子表达式的第一次出现。</td>
    </tr>
</table>

```c++
// 替换文件中的电话号码。
// 只生成电话号码。
string fmt2 = "$2.$5.$7 ";
cout << regex_replace(s, r, fmt2, format_no_copy) << endl;
```

## 随机数

以前C与C++都依赖一个简单的C库函数```rand```来生成随机数。此函数生成均匀分布的伪随机整数，每个随机数的范围在0和一个系统相关的最大值（至少32767）之间。

```rand```函数的缺点在于其不能生成不同范围的随机数，也不能生成随机浮点数、非均匀分布的数，此时需要转换```rand```生成的随机数的范围、类型或分布，而这尝尝会引入非随机性。

C++标准库通过一组**随机数引擎类（random-number engine）**与**随机数分布类（random-number distribution）**来解决以上问题。

### 随机数引擎类

随机数引擎类用于生成随机的```unsigned```整数序列。随机数引擎类是函数对象类，定义了一个调用运算符，其不接受参数并返回一个```unsigned```整数。可以通过随机数引擎对象生成原始的随机数。

随机数引擎支持的操作如下，其中```e```为引擎：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>Engine e</td>
        <td>默认构造函数，使用该引擎默认的种子。Engine为引擎类。</td>
    </tr>
    <tr>
        <td>Engine e(s)</td>
        <td>使用整型s作为种子。Engine为引擎类。</td>
    </tr>
    <tr>
        <td>e.seed(s)</td>
        <td>使用种子s重置e的状态。</td>
    </tr>
    <tr>
        <td>e.min()</td>
        <td>e可生成的最小值。</td>
    </tr>
    <tr>
        <td>e.max()</td>
        <td>e可生成的最大值</td>
    </tr>
    <tr>
        <td>Engine::result_type</td>
        <td>引擎类Engine生成的unsigned整型。</td>
    </tr>
    <tr>
        <td>e.discard(u)</td>
        <td>将引擎推进u步，u的类型为unsigned long long。</td>
    </tr>
</table>

```c++
default_random_engine e;
for (size_t i = 0; i < 10; ++i) {
    cout << e() << " ";
}
```

大多数情况下，随机数引擎是不能直接使用的，因此称其生成原始随机数。因为生成的随机数的值范围通常不符合需求，而正确转换随机数的范围是极其困难的。

#### 种类

标准库定义的随机数引擎类的区别在于性能与随机性质量不同。标准库定义了三个类，实现了不同的算法来生成随机数；标准库定义了三个适配器，可以修改给定引擎生成的序列。引擎与引擎适配器都是模板。

每个编译器都会指定其中一个作为```default_random_engine```类型，此类型旨在拥有最通用的有用特性。

> Each compiler designates one of these engines as the default_random_engine type. This type is intended to be the engine with the most generally useful properties.

这些引擎与特例化版本如下，这些引擎的名字与其数学性质相对应：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>default_random_engine</td>
        <td>某个其他引擎的别名，目的是用于大多数情况下。</td>
    </tr>
    <tr>
        <td>linear_congruential_engine</td>
        <td>minstd_rand0的乘数为16807，模为2147483647，增量为0；minstd_rand的乘数为48271，模为2147483647，增量为0。</td>
    </tr>
    <tr>
        <td>mersenne_twister_engine</td>
        <td>mt19937 32位无符号梅森旋转生成器;mt19937_64 64位无符号梅森旋转生成器。</td>
    </tr>
    <tr>
        <td>subtract_with_carry_engine</td>
        <td>ranlux24_base 32位无符号借位减法生成器；ranlux48_base 64位无符号借位减法生成器。</td>
    </tr>
    <tr>
        <td>discard_block_engine</td>
        <td>引擎适配器，将底层引擎的结果丢弃。用要使用的底层引擎、块大小与旧块大小来参数化。<br>ranlux24使用ranlux24_base引擎，块大小为223，旧块大小为23；ranlux48使用ranlux48_base引擎，块大小为389，旧块大小为11。</td>
    </tr>
    <tr>
        <td>independent_bits_engine</td>
        <td>引擎适配器，生成指定位数的随机数。用要使用的底层引擎、结果的位数与保存生成位的无符号整型来参数化。指定的位数必须小于指定的无符号整型所能保存的位数。</td>
    </tr>
    <tr>
        <td>shuffle_order_engine</td>
        <td>引擎适配器，返回底层引擎生成的数，但是返回的顺序不同。用要使用的底层引擎与混洗的元素数量来参数化。knuth_b使用表大小为256的minstd_rand0引擎。</td>
    </tr>
</table>

### 随机数分布类

随机数分布类使用引擎返回服从特定概率分布的随机数。随机数分布类也是函数对象，定义了一个调用运算符，其接受一个随机数引擎作为参数。随机数分布对象使用该引擎参数生成随机数，并将其映射到指定的分布。

分布类或对象支持操作如下，其中```d```为分布对象：

<table>
    <tr>
        <th>操作</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>Dist d</td>
        <td>定义一个分布对象。Dist为分布类，构造函数依赖于Dist的类型，且是explicit的。</td>
    </tr>
    <tr>
        <td>d(e)</td>
        <td>使用相同的引擎e连续调用d，会根据d的分布类型生成一个随机数序列。</td>
    </tr>
    <tr>
        <td>d.min()</td>
        <td>d(e)能生成的最小值。</td>
    </tr>
    <tr>
        <td>d.max()</td>
        <td>d(e)能生成的最大值。</td>
    </tr>
    <tr>
        <td>d.reset()</td>
        <td>重建d的状态，使得随后对d的使用不依赖于d已经生成的值。</td>
    </tr>
</table>

```c++
uniform_int_distribution<unsigned> u(0,9);
default_random_engine e;
for (size_t i = 0; i < 10; ++i) {
    cout << u(e) << " ";  // 注意不要写成u(e())，因为u接受一个引擎而不是一个值。因为某些分布需要调用多次引擎才能得到一个值。
}
```

```c++
// 生成非均匀分布：
default_random_engine e;
normal_distribution<> n(4,1.5);
vector<unsigned> vals(9);
for (size_t i = 0; i != 200; ++i) {
    unsigned v = lround(n(e));  // 舍入到最接近的整数。
    if (v < vals.size()) {
        ++vals[v];
    }
}
for (size_t j = 0; j != vals.size(); ++j) {
    cout << j << ": " << string(vals[j], '*') << endl;
}
```

```c++
// 生成伯努利分布：
default_random_engine e;  // 不要在循环内声明引擎。
bernoulli_distribution b;  // 不要在循环内声明分布对象。
do {
    bool first = b(e); 
    cout << (first ? "We go first" : "You get to go first") << endl;
    cout << ((play(first)) ? "sorry, you lost" : "congrats, you won") << endl;
    cout << "play again? Enter 'yes' or 'no'" << endl;
} while (cin >> resp && resp[0] == 'y');
```

分布对象与引擎的组合（the combination of a distribution object with an engine）被称为**随机数发生器（random-number generator）**。

一个给定的随机数发生器会总是生成相同的数值序列。一个函数如果定义了局部随机数发生器，应该将其（包括引擎与分布对象）定义为```static```的，否则每次调用会生成相同的序列。

```c++
// 几乎肯定是生成包含随机数的vector的错误方法。
vector<unsigned> bad_randVec() {
    default_random_engine e;
    uniform_int_distribution<unsigned> u(0,9); 
    vector<unsigned> ret; for (size_t i = 0; i < 100; ++i) {
        ret.push_back(u(e));
    }
    return ret;
}

vector<unsigned> v1(bad_randVec());
vector<unsigned> v2(bad_randVec());
cout << ((v1 == v2) ? "equal" : "not equal") << endl;  // 输出“equal”。

// 生成包含随机数的vector的正确方法。
vector<unsigned> bad_randVec() {
    static default_random_engine e;
    static uniform_int_distribution<unsigned> u(0,9); 
    vector<unsigned> ret; for (size_t i = 0; i < 100; ++i) {
        ret.push_back(u(e));
    }
    return ret;
}
```

如果要让随机数发生器生成不同的随机数序列，则需要设置不同的种子。引擎可以利用它从序列中的一个新位置重新开始生成随机数。

> A seed is a value that an engine can use to cause it to start generating numbers at a new point in its sequence.

```c++
default_random_engine e1;
default_random_engine e2(2147483646);
default_random_engine e3;
e3.seed(32767);
default_random_engine e4(32767);
for (size_t i = 0; i != 100; ++i) {
    if (e1() == e2()) {  // false
        cout << "unseeded match at iteration: " << i << endl;
    }
    if (e3() != e4()) {  // true
        cout << "seeded differs at iteration: " << i << endl;
    }
}
```

选择一个好的种子与生成好的随机数类似，也是极其困难的。

```c++
default_random_engine e1(time(0))  //调用系统函数time（位于头文件ctime）作为稍微随机的种子。如果程序作为一个自动过程的一部分反复运行，则该方法无效，因为它可能数次生成相同的种子。
```

#### 种类

除了总是生成```bool```类型的```bernouilli_distribution```外，其他分布类型都是模板。每个模板接受单个类型参数，它指出分布生成的结果类型。每个分布模板定义了一个默认模板实参。整型分布的默认实参为```int```，浮点数分布的默认实参为```double```。

> The library also defines several types that are built from the engines or adaptors. The ```default_random_engine``` type is a type alias for one of the engine types parameterized by variables designed to yield good performance for casual use. The library also defines several classes that are fully specialized versions of an engine or adaptor.

每个分布都有特定的构造函数，一些参数指出分布的范围，分布范围是闭合的，包括两个参数。

这些随机数分布类如下，其中```IntT```表示```short```、```int```、```long```、```long long```、```unsigned short```、```unsigned int```、```unsigned long```或```unsigned long long```；```RealT```表示```float```、```double```或```long double```，这些分布的名字与其数学性质相对应：

<table>
    <tr>
        <th>类型</th>
        <th>含义</th>
    </tr>
    <tr>
        <td>uniform_int_distribution&lt;IntT&gt; u(m, n)</td>
        <td>生成[m, n]范围内的均匀分布的随机数。m默认为0，n默认为IntT的最大值。</td>
    </tr>
    <tr>
        <td>uniform_real_distribution&lt;RealT&gt; u(x, y)</td>
        <td>生成[x, y]范围内的均匀分布的随机数。x默认为0.0，y默认为1.0。</td>
    </tr>
    <tr>
        <td>bernoulli_distribution b(p)</td>
        <td>以概率p生成true，p默认为0.5。</td>
    </tr>
    <tr>
        <td>binomial_distribution&lt;IntT&gt; b(t, p)</td>
        <td>分布按采样大小为整型t、概率为p计算得到，t默认为1，p默认为0.5。</td>
    </tr>
    <tr>
        <td>geometric_distribution&lt;IntT&gt; g(p)</td>
        <td>每次试验成功的概率为p，p默认为0.5。</td>
    </tr>
    <tr>
        <td>negative_binomial_distribution&lt;IntT&gt; nb(k, p)</td>
        <td>k次试验成功的概率为p，k为整型，默认为1，p默认为0.5。</td>
    </tr>
    <tr>
        <td>poisson_distribution&lt;IntT&gt; p(x)</td>
        <td>均值为double x的分布。</td>
    </tr>
    <tr>
        <td>exponential_distribution&lt;RealTgt; e(lam)</td>
        <td>参数lambda通过浮点值lam给出，lam默认为1.0。</td>
    </tr>
    <tr>
        <td>gamma_distribution&lt;RealT&gt; g(a, b)</td>
        <td>α=a，β=1。a与b默认为1.0。</td>
    </tr>
    <tr>
        <td>weibull_distribution&lt;RealT&gt; w(a, b)</td>
        <td>形状为a，尺度为b的分布。a与b默认为1.0。</td>
    </tr>
    <tr>
        <td>extreme_value_distribution&lt;RealT&gt; e(a, b)</td>
        <td>a默认为0.0，b默认为1.0。</td>
    </tr>
    <tr>
        <td>normal_distribution&lt;RealT&gt; n(m, s)</td>
        <td>均值为m，标准差为s。m默认为0.0，s默认为1.0。</td>
    </tr>
    <tr>
        <td>lognormal_distribution&lt;RealT&gt; ln(m, s)</td>
        <td>均值为m，标准差为s。m默认为0.0，s默认为1.0。</td>
    </tr>
    <tr>
        <td>chi_squared_distribution&lt;RealT&gt; c(x)</td>
        <td>自由度为x，默认为1.0。</td>
    </tr>
    <tr>
        <td>cauchy_distribution&lt;RealT&gt; c(a, b)</td>
        <td>位置为a，尺度为b。a默认为0.0，b默认为1.0。</td>
    </tr>
    <tr>
        <td>fisher_f_distribution&lt;RealT&gt; f(m, n);</td>
        <td>自由度为m与n，m与n默认为1。</td>
    </tr>
    <tr>
        <td>student_t_distribution&lt;RealT&gt; s(n);</td>
        <td>自由度为n，默认为1。</td>
    </tr>
    <tr>
        <td>discrete_distribution&lt;IntT&gt; d(i, j);</td>
        <td rowspan="2">i与j为权重序列输入迭代器，il为权重花括号列表，权重必须能转换为double。</td>
    </tr>
    <tr>
        <td>discrete_distribution&lt;IntT&gt; d{il}</td>
    </tr>
    <tr>
        <td>piecewise_constant_distribution&lt;RealT&gt; pc(b, e, w)</td>
        <td>b、e与w为输入迭代器。</td>
    </tr>
    <tr>
        <td>piecewise_linear_distribution&lt;RealT&gt; pl(b, e, w)</td>
        <td>b、e、w为输入迭代器。</td>
    </tr>
</table>
# 固有的不可移植的特性

为了支持低层编程（low-level programming），C++定义了一些固有的**不可移植的（nonportable）**的特性，不可移植特性与机器相关。算术类型的大小在不同的机器上可能不同，这是不可移植特性的一个示例。

## 位域

类可以将其（非静态）数据成员定义为**位域（bit-field）**，位域中包含一定数量的二进制位。位域通常（normally）用于程序将二进制数据传递给其他程序或硬件设备。

位域必须是整型或枚举类型。通常使用```unsigned```类型保存位域，因为```signed```位域的行为取决于具体实现。位域的声明形式为成员名字后面紧跟一个冒号以及一个常量表达式，该表达式用于指定成员所占的二进制位数。

> We indicate that a member is a bit-field by following the member name with a colon and a constant expression specifying the number of bits:

```c++
typedef unsigned int Bit;
class File {
    Bit mode: 2;
    Bit modified: 1;
    Bit prot_owner: 3;
    Bit prot_group: 3;
    Bit prot_world: 3;
public:
    enum modes { READ = 01, WRITE = 02, EXECUTE = 03 };
    File &open(modes);
    void close();
    void write();
    bool isRead() const;
    void setWrite();
}

void File::write() {
    modified = 1;
    // ...
}
void File::close() {
    if (modified) {
        // ... 保存内容。
    }
}

// 通常使用内置的位运算符操作超过1位的位域。
File &File::open(File::modes m) {
    mode |= READ;
    // ...
    if (m & WRITE) {
        // processing to open the file in read/write mode
    }
    return *this;
}
```

如果可能的话，类内部连续定义的位域会压缩在同一整数的相邻位（能否压缩或如何压缩机器相关）。

> Bit-fields defined in consecutive order within the class body are, if possible, packed within adjacent bits of the same integer, thereby providing for storage compaction. 

```&```不能作用于位域，因此任何指针都无法指向类的位域。

如果一个类定义了位域成员，则它通常也会定义一组内联成员函数以检验或设置位域的值。

```c++
inline bool File::isRead() const {
    return mode & READ;
}
inline void File::setWrite() {
    mode |= WRITE;
}
```

## ```volatile```限定符

如果对象的值可能在程序的控制或检测之外被改变时，应该将其声明为```volatile```的。

> An object should be declared **volatile** when its value might be changed in ways outside the control or detection of the program.

```volatile```限定符的用法与```const```限定符的用法很相似。它是类型的额外的修饰符。

```c++
volatile int display_register;
volatile Task *curr_task;
volatile int iax[max_size];
volatile Screen bitmapBuf;
```

```const```与```volatile```限定符互相没有影响，一个类型可以同时是```const```的与```volatile```的。

就像一个类可以定义```const```成员函数一样，它也可以定义```volatile```成员函数。```volatile```对象只能调用```volatile```成员函数。

> In the same way that a class may define ```const``` member functions, it can also define member functions as ```volatile```. Only ```volatile``` member functions may be called on ```volatile``` objects.

```volatile```与指针之间也有着与```const```与指针之间相同的作用关系。

```volatile```的转换与```const```的转换规则相同。

```c++
volatile int v;
int *volatile vip;  // 指向int的volatile指针。
volatile int *ivp;  // 指向volatile int的指针。
volatile int *volatile vivp;  // 指向volatile int的volatile指针。
int *ip = &v;  // 错误。
*ivp = &v;
vivp = &v;
```

```const```与```volatile```的一个重要区别是：不能使用合成的拷贝/移动构造函数或赋值运算符从一个```volatile```对象初始化或赋值，因为合成的成员接受的形参为（非```volatile```的）```const```的引用。

> One important difference between the treatment of ```const``` and ```volatile``` is that the synthesized copy/move and assignment operators cannot be used to initialize or assign from a ```volatile``` object. The synthesized members take parameters that are references to (non```volatile```) ```const```, and we cannot bind a non```volatile``` reference to a ```volatile``` object.

如果一个```volatile```对象要被拷贝、移动或赋值，则它必须自定义拷贝或移动操作。

```c++
class Foo {
public:
    Foo(const volatile Foo&);
    Foo &operator=(volatile const Foo&);  // 将一个volatile对象赋值给一个非volatile对象。
    Foo &operator=(volatile const Foo&) volatile;  // 将一个volatile对象赋值给一个volatile对象。
    // ...
};
```

虽然可以为```volatile```对象定义拷贝与赋值操作，但拷贝一个```volatile```对象有没有意义与具体的使用目的密切相关。

> Although we can define copy and assignment for ```volatile``` objects, a deeper question is whether it makes any sense to copy a ```volatile``` object. The answer to that question depends intimately on the reason for using ```volatile``` in any particular program.

## 链接指示

C++程序有时需要调用其他编程语言（最常见的是C）所写的函数。这些函数的名字也必须被声明，声明也必须指定返回类型与形参列表。编译器检查其调用的方式与处理普通C++函数的方式相同，但是生成的代码通常有所区别。

C++使用**链接指示（linkage directive）**指出非C++函数所用的语言。

> C++ uses **linkage directives** to indicate the language used for any non-C++ function.

要想将C++代码与其他语言编写的代码放在一起使用，要求我们有权访问该语言的编译器，并且该编译器与当前的编译器是兼容的。

链接指示有两种：单个的（```extern```紧跟一个字符串字面值常量（编写函数所用的语言），随后是一个“普通”的函数声明）或复合的（```extern```紧跟一个字符串字面值常量（编写函数所用的语言），以及一个花括号，花括号内有函数声明）。

链接指示不能出现在类或函数定义的内部，同样的链接指示必须在函数的每个声明中都出现。

编译器要求支持对C语言的链接指示，也可能支持其他语言的链接指示。

```c++
extern "C" size_t strlen(const char);  // 单语句链接指示。
extern "C" {  // 复合语句链接指示。
    int strcmp(const char*, const char*);
    char *strcat(char*, const char*);
}
```

复合的链接指示可以应用于整个头文件。

```c++
// C++的cstring头文件可能形如：
extern "C" {
    #include <string.h>
}
// C++从C标准库继承的函数可以定义为C函数，单并非必须。决定使用C还是C++实现C标准库函数取决于每个C++的实现。
```

编写函数所用的语言是函数类型的一部分。对于使用链接指示定义的函数来说，它的每个声明必须使用相同的链接指示，并且指向其他语言编写的函数的指针必须与函数本身使用相同的链接指示。

> The language in which a function is written is part of its type. Hence, every declaration of a function defined with a linkage directive must use the same linkage directive. Moreover, pointers to functions written in other languages must be declared with the same linkage directive as the function itself:

```c++
extern "C" void (*pf)(int);  // pf指向C函数。

void (*pf1)(int);
extern "C" void (*pf2)(int);
pf1 = pf2;  // 错误。
```

链接指示不仅应用于函数，还应用于作为返回类型或形参类型的函数指针。因此，如果要传给C++函数一个指向C函数的指针，必须使用类型别名。

```c++
extern "C" void f1(void(*)(int));  // f1为C函数，其形参为一个指向C函数的指针。
```

```c++
extern "C" typedef void FC(int);
void f2(FC*);
```

### 导出C++到其他语言

对函数定义使用链接指示，可以使得C++函数在其他语言编写的程序中可用。

```c++
extern "C" double calc(double dparm) { /* ... */ }   // 编译器将为其生成适用于指定语言的代码。
```

值得注意的是，可被多种语言共享的函数的形参与返回类型经常受到限制。例如，几乎不可能编写一个函数，该函数将（非平凡的，nontrivial）C++类对象传递给一个C程序。

> For example, we can almost surely not write a function that passes objects of a (nontrivial) C++ class to a C program. 

链接指示与重载的相互作用依赖于目标语言，如果目标语言支持重载，则为该语言实现链接指示的编译器可能支持重载这些C++函数。

```c++
// 错误，C不支持重载。
extern "C" void print(const char*);
extern "C" void print(int);
```

```c++
// 如果一组重载函数中有一个C函数，则其余函数必须是C++函数。
// C版本的calc可以在C与C++程序中被调用，其他函数只能被C++程序中调用。
class SmallInt { /* ... */ };
class BigNum { /* ... */ };
extern "C" double calc(double);
extern SmallInt calc(const SmallInt&);
extern BigNum calc(const BigNum&);
```

# 程序组织

## 文件

存放程序的文件通常（normally）被称为源文件（source file）。对于C++的源文件，不同的编译器使用不同的后缀命名约定，最常见的包括“.cc”、“.cxx”、“.cpp”、“.cp”与“.C”。

### 头文件

我们编写的**头文件（header）**的后缀名通常为“.h”，也有程序员使用“.H”、“.hpp”或“.hxx”作为后缀名。标准库头文件通常没有任何后缀。编译器通常不关心文件名的形式，但是IDE可能关心。

按照惯例，头文件根据其中定义的类的名字来命名。

> Conventionally, header file names are derived from the name of a class defined in that header.

程序要使用头文件中的内容，必须使用```#include```紧跟头文件名（必须位于一行，且通常必须位于任何函数外，典型情况下全部位于源文件开始处），```#include```依赖于一个预处理功能，当编译器看到```#include```的时候，它会用指定头文件的内容代替```#include```。

头文件一旦改变，使用该头文件的源文件必须重新编译以获得更新过的声明。

> Whenever a header is updated, the source files that use that header must be recompiled to get the new or changed declarations.

#### 头文件保护符

头文件通常包含只能在任意给定文件中被定义一次的实体（如类定义、```const```与```constexpr```变量）。头文件通常也用到其他头文件中的功能，这样就可能导致头文件（错误地）多次包含一些实体。例如：```Sales_data```类有一个```string```成员，因此```Sales_data.h```（```Sales_data```位于的头文件）必须包含```string```头文件；使用```Sales_data```类的程序为了能操作```bookNo```成员需要再一次包含```string```头文件。这样，使用```Sales_data```类的程序就两次包含了```string```头文件。

确保头文件被多次包含仍能安全工作的最常用的技术依赖于预处理器。C++程序除了使用```#include```包含头文件外，还使用预处理器定义**头文件保护符（header guard）**。头文件保护符依赖于预处理变量。预处理变量有两种状态：已定义的与未定义的。其中```#define```指令接受一个名字，并将该名字定义为预处理变量。```#ifdef```与```ifndef```指令用于判断变量是否已定义。如果一个变量已被定义，则```#ifdef```为真；如果一个变量未被定义，则```ifndef```为真。如果检查结果为真，则跟随在```#ifdef```或```#ifndef```后的语句都被执行，直到遇到```#endif```。

头文件中应该使用保护符，即使目前没有被包含在其他头文件中。

```c++
// 头文件保护符防止重复包含。
#ifndef SALES_DATA_H
#define SALES_DATA_H
#include <string>
struct Sales_data {
 string bookNo; unsigned units_sold = 0; double revenue = 0.0;
};
#endif
```

在整个程序中，预处理变量，包括头文件保护符的名字必须唯一。典型地，基于头文件中类的名字来构建保护符的名字以确保其唯一性。为了避免与程序中其他实体发生名字冲突，一般把预处理变量的名字全部大写。

### 分离式编译

分离式编译（separate compilation）允许将程序分割到多个文件中去，每个文件被独立编译。

```c++
// 分离式编译的例子。
/* fact函数位于fact.cc文件中，它的声明位于其他头文件中，fact.cc包含该头文件。
 * main函数位于factMain.cc文件中，它调用fact函数。
 */

/* 生成可执行文件的编译过程。
 * $为系统提示符，CC为编译器名字，#后面是命令行注释。
 * 假设一个源文件被修改，则只需重新编译该源文件。
 * 大多数编译器提供了分离式编译机制，这一过程通常生成一个扩展名为.obj（在Windows中）或.o（在UNIX中）的文件，表明该文件是包含对象代码（object code）。
 */
// $ CC factMain.cc fact.cc  # generates factMain.exe or a.out
// $ CC factMain.cc fact.cc -o main  # generates main or main.exe

/*
 * 接下来编译器负责将对象文件链接在一起形成可执行文件。
 * 在某些系统中，分别编译该程序的过程如下：
 */
// $ CC -c factMain.cc  # generates factMain.o
// $ CC -c fact.cc  # generates fact.o
// $ CC factMain.o fact.o  # generates factMain.exe or a.out
// $ CC factMain.o fact.o -o main  # generates main or main.exe
```

## 注释

C++中有两种注释（comment）：

- 单行（single-line）注释：以```//```开始，以换行符（newline）结束。```//```右侧的所有内容（可以包含任何文本，包括```//```）都被编译器忽略。
- 界定符对（paired）注释：以```/*```开始，以```*/```结束。其中可以包含除```*/```外的任何内容，包括换行符。注释界定符可以放置于任何允许放置制表符、空格或换行符的地方。注释界定符可以跨越多行，此时可以显式指出其内部的程序行都属于多行注释，采用的风格可以是：每行以一个```*```开头。

程序通常包括两种形式的注释，其中注释界定符对通常用于多行解释，单行注释则常用于半行与单行注释。

> There are two kinds of comments in C++: single-line and paired.

> A comment pair can be placed anywhere a tab, space, or newline is permitted. Comment pairs can span multiple lines of a program but are not required to do so. When a comment pair does span multiple lines, it is often a good idea to indicate visually that the inner lines are part of a multiline comment. Our style is to begin each line in the comment with an asterisk, thus indicating that the entire range is part of a multiline comment.

> Programs typically contain a mixture of both comment forms. Comment pairs generally are used for multiline explanations, whereas double-slash comments tend to be used for half-line and single-line remarks:

```c++
#include <iostream>
/*
 * Simple main function:
 * Read two numbers and write their sum
 */
int main() {
    // prompt user to enter two numbers
    cout << "Enter two numbers:" << endl;
    int v1 = 0, v2 = 0; // variables to hold the input we read
    cin >> v1 >> v2; // read input
    cout << "The sum of " << v1 << " and " << v2 << " is " << v1 + v2 << endl;
    return 0;
}
```

```c++
/*
* comment pairs /* */ cannot nest.
* ''cannot nest'' is considered source code,
* as is the rest of the program
*/
int main() {
    return 0;
}
```

## 调试帮助

C++程序有时会用到类似于头文件保护符的技术，以便有条件地（conditionally）执行调试代码，程序可以包含一些调试代码，这些代码只在开发程序时使用，当应用程序编写完成并准备发布时，需要将调试代码屏蔽掉。该方法用到两个预处理功能：```assert```与```NDEBUG```。

```assert```为一个**预处理宏（preprocessor macro）**，定义在头文件```cassert```中。预处理宏是一个预处理变量，其行为类似内联函数。```assert```宏使用一个表达式作为它的条件，其形式为：```assert(expr);```。```assert```宏对表达式```expr```求值，如果其为```false```，则```assert```输入消息并终止程序；如果其为```true```，则```assert```什么也不做。和预处理变量一样，宏的名字必须在程序中唯一。程序中尽量不要自定义名为```assert```的实体，即使程序没有显式包含```cassert```头文件（很多头文件都包含了```cassert```头文件，因此程序可能间接包含该头文件）。

```assert```宏通常用来检查“不能发生”的条件。

```c++
// A program that does some manipulation of input text might know that all words it is given are always longer than a threshold.
// 程序中可能如下所示的语句：
assert(word.size() > threshold);
```

```assert```的行为依赖于一个名为```NDEBUG```的预处理变量的状态。如果```NDEBUG```被定义，则```assert```什么也不做。默认情况下，```NDEBUG```未被定义，此时```assert```执行运行时检查。

```c++
// 命令行选项。
// 等价于在文件main.C的开头写：#define NDEBUG
// $ CC -D NDEBUG main.C # use /D with the Microsoft compiler
```

C++编译器定义了名为```__func```的变量，保存当前调试的函数名。预处理器还定义了其他四个对调试有帮助的4个常量：

- ```__FILE__```：存放文件名的字符串字面值。
- ```__LINE__```：存放当前行号的整型字面值。
- ```__TIME__```：存放文件编译时间的字符串字面值。
- ```__DATE__```：存放文件编译日期的字符串字面值。

```c++
// 使用NDEBUG自定义条件调试代码。
void print(const int ia[], size_t size) {
    #ifndef NDEBUG
    // _ _func_ _ is a local static defined by the compiler that holds the function's name
    cerr << _ _func_ _ << ": array size is " << size << endl;
    #endif
    // ...
}
```

```c++
// 在错误消息报告信息。
if (word.size() < threshold) {
    cerr << "Error: " << _ _FILE_ _
         << " : in function " << _ _func_ _
         << " at line " << _ _LINE_ _ << endl
         << " Compiled on " << _ _DATE_ _
         << " at " << _ _TIME_ _ << end
         << " Word read was \"" << word
         << "\": Length too short" << endl;
}
```

> If NDEBUG is defined, we avoid the potential run-time overhead involved in checking various conditions. Of course, there is also no run-time check. Therefore, assert should be used only to verify things that truly should not be possible. It can be useful as an aid in getting a program debugged but should not be used to substitute for run-time logic checks or error checking that the program should do.

# C与C++

## 继承自C++的特性

### 预处理器

**预处理器（preprocessor）**是在编译之前执行的一段程序，可以改变我们所写的程序。

> The preprocessor—which C++ inherits from C—is a program that runs before the compiler and changes the source text of our programs.

预处理变量由预处理器管理，不是命名空间```std```的一部分，因此可以直接被使用，无需使用```std::```前缀。当用到一个预处理变量时，预处理器会自动将它替换为其值（因此，使用```NULL```初始化一个指针等价于使用```0```初始化它）。

### 头文件

C++标准库包含（incorporate）了C语言的标准库。C语言中形如“*name*.h”的头文件在C++版本中被命名为“c*name*”。其中，“c”前缀表明该头文件是C标准库的一部分。这两个头文件中的内容相同，但是后者形式更适合于C++程序。定义在“c*name*”中的名字定义在```std```头文件中，定义在“*name*.h”中的名字不是定义在```std```头文件中。

C++程序员应该使用“c*name*”版本的头文件。使用“*name*.h”版本的头文件使得程序员不得不记住哪些标准库是从C继承而来的，哪些标准库名字是C++独有的。

# 参考

[^1]: [C++ Primer, 5th Edition by Stanley B. Lippman, Josee LaJoie, Barbara E. Moo (z-lib.org).pdf](https://1lib.education/book/2733889/20fe00)$\rightarrow$[C++ Primer, 5th Edition by Stanley B. Lippman, Josee LaJoie, Barbara E. Moo (z-lib.org).pdf](资源\C++ Primer, 5th Edition by Stanley B. Lippman, Josee LaJoie, Barbara E. Moo (z-lib.org).pdf)
[^1]: [[www.gogo.so]C++ Primer(第5版)（带代码图）.pdf](https://pan.baidu.com/s/131exs)$\rightarrow$[[www.gogo.so]C++ Primer(第5版)（带代码图）.pdf](资源\[www.gogo.so]C++ Primer(第5版)（带代码图）.pdf)
[^1]: [[www.gogo.so]C++ Primer(第5版)（不带代码图）.pdf](https://pan.baidu.com/s/1c0hIure)$\rightarrow$[[www.gogo.so]C++ Primer(第5版)（不带代码图）.pdf](资源\[www.gogo.so]C++ Primer(第5版)（不带代码图）.pdf)

# 排除章节

- 1.1.1
- 1.5.1 $\rightarrow$ Using File Redirection
- 16.1.6
- 19.1
- 19.8.3 $\rightarrow$ Preprocessor Support for Linking to C
