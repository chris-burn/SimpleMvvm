package example.codeclan.com.simplemvvm.viewmodels;

import android.arch.lifecycle.ViewModel;

import example.codeclan.com.simplemvvm.models.Person;

/**
 * Created by user on 14/07/2017.
 */

public class MainViewModel extends ViewModel
{
    public PersonViewModel personViewModel;

    public MainViewModel()
    {
        personViewModel = new PersonViewModel(new Person("Dave", "McDave", 38));
    }
}
