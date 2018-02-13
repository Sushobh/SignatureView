# SignatureView
A signature drawing custom view for Android.

## This is an ongoing project where i am working on creating a signature view for Android.

### Screenshots

![Screenshot](https://raw.github.com/Sushobh/SignatureView/master/Screenshots/Screenshot_1518514063.png)
![Screenshot](https://raw.github.com/ryanmaxwell/SignatureView/master/Screenshots/Screenshot_1518514153.png)


### How to use

```java
public class MainActivity extends AppCompatActivity {


    SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signatureView = new SignatureView(this);
       
        setContentView(signatureView);
    }
}


```
You can also add it in xml and then get a reference.

### Adding options and listening for callbacks.

You can add options at the top , and listen for events. In the screenshots i have added the the save option.

```java
signatureView.addOption(new Option("Save ",R.drawable.ic_save));
signatureView.setOptionClickListener(new OptionClickListener() {
            @Override
            public void clickedOnOption(Option option) {
                Log.i("Clicked on option !",option.getTitle());
            }
        });
```

### How to get a bitmap to save?

The signature view automatically removes the buttons at the top and black border to give you a bitmap which you can save.

```java
signatureView.getSignatureBitmap();
```


