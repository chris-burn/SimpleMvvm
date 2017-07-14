package example.codeclan.com.simplemvvm.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import example.codeclan.com.simplemvvm.models.Person;

/**
 * Created by user on 14/07/2017.
 */

public class PersonViewModel extends BaseObservable
{
    @Bindable
    private Person person;

    public PersonViewModel(Person person)
    {
        this.person = person;
    }

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
}
