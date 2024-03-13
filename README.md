## Assignment for Upstox.

### | project structure:

- `components`: Individual widgets for Custom Top Bar, Bottom App Bar and Holding Widgets
- `model`: data classes for API response.
- `repository`: Contains a Data repository to manipulate and store API response in proper class. Also includes an interface for API calls.
- `ui`: Theme and Typography
- `MainActivity.kt`: Main activity of the app. 

The main activity uses kotlin coroutines to make API calls. Upon fetching the data, Repository saves it in the `StockResponse` data class.
If the API or the parsing of response into data class fails, An error page is shown. otherwise the `HoldingPage` widget is loaded.

The `HoldingPage` widget takes the API response as input and builds a top bar, bottom navbar and a `HoldingWidget` inside a `LazyColumn` for every holding in the API response. There's also a view for no holdings.

The `CustomBottomBar` has a local variable to track the expanded state. The expanded state has a height 2.5 times that of non expanded bottom app bar.
It uses the functions from the `StockResponse` data class to calculate the final values of Current Value, Total Investment, Today's P/L and Total P/L.
This way the business logic of calculations and views are separated.

User click on the `CustomBottomBar` toggles the expanded state. This widget doesn't show if there are no holdings.

The `DataRepository` class and `ApiService` interface allow adding new endpoints quickly while maintaining compatibility with existing ones.
This is achieved by using retrofit. Moshi allows us to create factories to serialise and deserialize response objects.

### | Architecture:

The project uses MVVM architecture. The user input goes to the `view`. (`CustomBottomBar`)
<br>
The logic of converting API response into usable Object lies in the `DataRepository` and business logic of calculating values specific to a model lie in `StockResponse` `model`.

### | Building & running:
A run configuration is included in the project. Just clicking run in Android studio will run the app.

### | APK:
The final built APK can be found [here](https://drive.google.com/file/d/1v82KcWfH-VLrRhKtgVMr8t_eEIAK400t/view?usp=sharing) on drive.
