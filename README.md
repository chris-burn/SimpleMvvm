# SimpleMvvm
### Starting up

There's a fair amount of boilerplate infrastructure that needs to be in place before we really get started with the fun stuff. It gets reasonably automatic after you've knocked out three or four projects, but it's a bit of a PITA the first time round. 

Start a new project called SimpleMvvm with an empty activity called MainActivity. I'm using API level 23. 

##Admin

###Enabling databinding

Open up build.gradle (Module: app)
At the bottom of the android {} section, just underneath the buildTypes{} tag, add a new section:

```
dataBinding {
        enabled=true
    }
```

###Add ViewModel component

This is likely to change as and when Android push out more updates to the relatively new databinding toolset. You need to add a couple of references to Gradle to pull in the necessary components.

We're following the instructions [here](https://developer.android.com/topic/libraries/architecture/adding-components.html). 

Open the `build.gradle (Project: SimpleMvvm)` file. 
Find the allprojects{repositories{} } tags about halfway down the file (line 15 in mine).
Add `maven { url 'https://maven.google.com' }` inside the repositories tag under `jcenter()`:

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://maven.google.com' }
    }
} 
```

Open the `build.gradle (Module: app)` file. 
Add the following to the dependencies section at the bottom: 

```
compile "android.arch.lifecycle:runtime:1.0.0-alpha3"
compile "android.arch.lifecycle:extensions:1.0.0-alpha3"
annotationProcessor "android.arch.lifecycle:compiler:1.0.0-alpha3"
```

Resync the project.
All being well, we should now be set up to use the databinding tools and the ViewModel class.

###Model

In `example.codeclan.com.simplemvvm`, create a new package called `models`
In the new `models` package, create a class called `Person`
Add `private String firstName`, `private String lastName` and `private int age` to the `Person` class. 
Create getters and setters for `firstName`, `lastName` and `age`.

###Main ViewModel

Create a new package called `viewmodels` in `example.codeclan.com.simplemvvm`.
Add a new class to the `viewmodels` package called `MainViewModel`.

Amend the class definition to:
`MainViewModel extends ViewModel`

The docs for the Android ViewModel class are [here](https://developer.android.com/topic/libraries/architecture/viewmodel.html). 

>The ViewModel class is designed to store and manage UI-related data so that the data survives configuration changes such as screen rotations.

###View

Open up `activity_main.xml`.

Android databinding requires that interface .xml files be in a particular structure. They must have a `<layout>` tag as the outermost element, with a `<data>` section as the first element within it, followed by all your interface elements. 

Add a `<layout>` tag just underneath `<?xml version="1.0" encoding="utf-8"?>` Add the closing `</layout>` tag as well.
Add `<data></data>` just inside the `<layout>` tag. 
Copy/paste the existing `<ConstraintLayout...` inside the `<layout>` tag just underneath the `<data></data>` tags. 

It should now look like: 

```
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>

    </data>

    <android.support.constraint.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        ...
        ...
        ...
      </android.support.constraint.ConstraintLayout>

  </layout>
```

We need to provide the interface file with something to bind to/get values from. Inside the `<data>` tag, add a reference to `MainViewModel` as follows: 

```
<variable
  name="viewModel"
  type="example.codeclan.com.simplemvvm.viewmodels.MainViewModel"/>
```

The next time you build the project Android Studio should see this layout file and auto-create the files you need to bind this layout to a `ViewModel`. Go to the Build menu and select Make Project.

###Binding

Last but not least we need to tell the `Activity` to bind the `View` to the `ViewModel`. Open up `MainActivity.java`. In the `onCreate()` method, just after `setContentView()`, add:

```
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
binding.setViewModel(new MainViewModel());
```

If we're very lucky this is the last time we'll need to do anything in the `Activity` file - no more manual setting of `TextView` values!

###Object ViewModel

Ideally you don't want to be working directly with your `Models` - it's really common that you might only want to use a subset of the properties (e.g. the `Person` class might have an `int Id` for DB storage, but we don't need it to collect their `name`), or that you might want to add some things you need to make the `View` work that don't really belong on a `Person`. To bind to a class it also has to extend an Android class called `BaseObservable`, and a common or garden `Person` doesn't need this. We're going to wrap `Person` in a `ViewModel` to make it easier to work with in the `View`, and to provide some bindable values. 

Create a new class in the `ViewModels` folder called `PersonViewModel`.
Change the class definition to extend `BaseObservable`: 

`public class PersonViewModel extends BaseObservable`

Add a private instance of `Person` with an @Bindable annotation, and a constructor that takes in a `Person`: 

```
@Bindable
private Person person;

public PersonViewModel(Person person)
{
    this.person = person;
}
```

Now add getters and setters for all of the properties of `Person` (`firstName`, `LastName`, `age`):

```
public String getFirstName()
{
    return person.getFirstName();
}

public void setFirstName(String firstName)
{
    person.setFirstName(firstName);
}

public String getLastName()
{
    return person.getLastName();
}

public void setLastName(String lastName)
{
    person.setLastName(lastName);
}

public int getAge()
{
    return person.getAge();
}

public void setAge(int age)
{
    person.setAge(age);
}
```

With the admin done we should be ready to start binding things in the `View`. Let's give ourselves something to work with. 

Open up `MainViewModel`

Add a public instance of `PersonViewModel` called `personViewModel`.
Create a `MainViewModel` constructor (no parameters).
Instantiate `personViewModel` with a new instance of `Person`:

```
public PersonViewModel personViewModel;
    
public MainViewModel()
{
    personViewModel = new PersonViewModel(new Person("Dave", "McDave", 38));
}
```

###View controls

We're going to stick a few `TextView` controls into a chain to display our `PersonViewModel` values. 

From: [here](https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html#Chains)

>A set of widgets are considered a chain if they a linked together via a bi-directional connection

I have strong heebie jeebies about using a WYSIWYG editor to build interfaces. It's tricky to make sure that things have been joined up exactly the way you want them. With that in mind I'm going to do all my interface building in the Text view in Android Studio.

Open `main_activity.xml`
Switch to Text view (rather than Design view)
Inside the `ConstraintLayout` tags create 3 `TextViews` for `firstName`, `lastName` and `age`:

```
<TextView
    android:id="@+id/txtFirstName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
<TextView
    android:id="@+id/txtLastName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
<TextView
    android:id="@+id/txtAge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```

Add Top, Left, Right and Bottom constraints to each `TextView` to create bi-directional links between them and the parent:

```
<TextView
    android:id="@+id/txtFirstName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtLastName"/>
<TextView
    android:id="@+id/txtLastName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toBottomOf="@+id/txtFirstName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtAge"/>
<TextView
    android:id="@+id/txtAge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toBottomOf="@+id/txtLastName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"/>
```

These are now in a vertical chain. By default the vertical chain style is 'spread', which evenly distributes the items. We want the `TextViews` clumped together, so we need to change this behaviour to 'packed'. In the first element of the chain (`txtFirstName`) we need to change the vertical chain style:

```
<TextView
  android:id="@+id/txtFirstName"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  app:layout_constraintTop_toTopOf="parent"
  app:layout_constraintLeft_toLeftOf="parent"
  app:layout_constraintRight_toRightOf="parent"
  app:layout_constraintBottom_toTopOf="@+id/txtLastName"
  app:layout_constraintVertical_chainStyle="packed"/>
```

Add an 8dp margin to each `TextView` control: 

```
<TextView
    android:id="@+id/txtFirstName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtLastName"
    app:layout_constraintVertical_chainStyle="packed"/>
<TextView
    android:id="@+id/txtLastName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toBottomOf="@+id/txtFirstName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtAge"/>
<TextView
    android:id="@+id/txtAge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toBottomOf="@+id/txtLastName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"/>
```

##The good bit

Finally the good bit!

The notation for databinding allow us to ignore the words 'get' and 'set' when dealing with properties, which leads to much nicer looking code. You use the notation: `@{}` to denote a binding. 

Add bindings between your `firstName` and `lastName` `TextView`'s `Text` properties to the properties of the `PersonViewModel` object in the `MainViewModel` (don't do anything with the `age` `TextView` yet!):

```
<TextView
    android:id="@+id/txtFirstName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtLastName"
    app:layout_constraintVertical_chainStyle="packed"
    android:text="@{viewModel.personViewModel.firstName}"/>
<TextView
    android:id="@+id/txtLastName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toBottomOf="@+id/txtFirstName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toTopOf="@+id/txtAge"
    android:text="@{viewModel.personViewModel.lastName}"/>
```

Moment of truth time: 

Take a deep breath.
Run the app.

Revel in the wondrous glory of having put the words 'Dave' and 'McDave' on the screen without ever having to directly set the value of the `TextViews`. 

Finally, we'll deal with the `age` `TextView`. Because `TextView.Text` is a `String` property we need to use a slightly sneaky shorthand to convert `age` from an `int` to a `String`:

```
<TextView
    android:id="@+id/txtAge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:layout_constraintTop_toBottomOf="@+id/txtLastName"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    android:text="@{`` + viewModel.personViewModel.age}"/>
```

Rerun the app, see Dave McDave's age appear on-screen, and take a moment to reflect on your cunning and general invincibility. Inform anyone nearby that you've effectively beaten Java and will be retiring immediately. Do a small victory dance. 
