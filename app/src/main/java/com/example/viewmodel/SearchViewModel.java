package com.example.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.repository.RecipeRepository;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SearchViewModel extends AndroidViewModel {
    private final RecipeRepository repository;
    private LiveData<List<String>> allRecipeTitles;

    public SearchViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipeTitles = repository.getAllRecipeTitles();
    }

    public LiveData<List<String>> getAllRecipeTitles() {
        return allRecipeTitles;
    }

    public LiveData<List<String>> searchRecipes(String query) {
        return Transformations.map(allRecipeTitles, titles ->
                titles.stream()
                        .filter(title -> title.toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList())
        );
    }

    public void insertSampleData() {
        // Recipe 1: Biryani
        repository.insertRecipe(
                "Biryani",
                "recipe_paneer_biryani",
                "60 minutes",
                "Medium ~ High",
                Arrays.asList(
                        "1 cup thick curd / yogurt",
                        "1 tsp ginger garlic paste",
                        "¼ tsp turmeric",
                        "¾ tsp kashmiri red chilli powder",
                        "2 tbsp biryani masala",
                        "4 tbsp coriander, finely chopped",
                        "5 tsp oil",
                        "1 tbsp lemon juice",
                        "2 tsp salt",
                        "15 cubes paneer (cottage cheese)",
                        "½ onion, petals",
                        "½ capsicum, cubed",
                        "6¼ cups water",
                        "5 pods cardamom",
                        "4 cloves",
                        "1 inch cinnamon",
                        "4 bay leaves (tej patta)",
                        "1 tsp pepper",
                        "1 chili, slit",
                        "1 cup basmati rice (soaked for 20 minutes)",
                        "1 star anise",
                        "1 mace (javitri)",
                        "1 tsp shah jeera",
                        "½ onion, sliced",
                        "1 tomato, finely chopped",
                        "2 tbsp fried onion",
                        "Pinch biryani masala",
                        "2 tbsp saffron milk"
                ),
                Arrays.asList(
                        "4 tbsp mint / pudina, chopped",
                        "2 tsp ghee (clarified butter)"

                ),
                Arrays.asList(
                        "In a large bowl, combine curd, ginger garlic paste, turmeric, red chilli powder, biryani masala, coriander, mint, oil, lemon juice, and salt.",
                        "Add paneer cubes, onion petals, and capsicum, and mix gently. Cover and marinate for 30 minutes.",
                        "For the rice: Boil 6 cups water with cardamom, cloves, cinnamon, bay leaves, pepper, salt, oil, and chili for 2 minutes.",
                        "Add soaked rice and boil for 3 minutes until half cooked. Drain the rice and set aside.",
                        "For the biryani: Heat oil and ghee in a large kadai. Add bay leaves, star anise, black cardamom, mace, cardamom, and shah jeera.",
                        "Saute sliced onion until golden, then add chopped tomato and cook until soft.",
                        "Add marinated paneer and cook until oil separates. Be careful not to overcook the paneer.",
                        "Spread the half-cooked rice evenly over the paneer mixture.",
                        "Top with coriander, mint, fried onion, biryani masala, saffron milk, ghee, and water.",
                        "Cover with aluminum foil and seal the lid (or use dough to seal). Simmer for 20 minutes until rice is fully cooked.",
                        "Serve the Paneer Biryani with raita and onion slices."
                )
        );

        // Recipe 2: Paneer Butter Masala
        repository.insertRecipe(
                "Paneer Butter Masala",
                "recipe_paneer_butter_masala",
                "50 minutes",
                "Medium",
                Arrays.asList(
                        "4 tbsp oil (2 tbsp for masala paste, 2 tbsp for curry))",
                        "4 tbsp butter (2 tbsp for masala paste, 2 tbsp for curry)",
                        "5 cloves",
                        "2 pods cardamom",
                        "2 onions, sliced",
                        "2 cloves garlic",
                        "1 inch ginger",
                        "3 tomatoes, sliced",
                        "1 bay leaf",
                        "1 chili",
                        "1 onion, finely chopped",
                        "½ tsp turmeric",
                        "1 tsp chili powder",
                        "¾ tsp coriander powder",
                        "¼ tsp cumin powder",
                        "½ tsp garam masala",
                        "1 tsp salt",
                        "1 cup water",
                        "16 cubes paneer"
                ),
                Arrays.asList(
                        "2 tbsp cream",
                        "1 tsp kasuri methi",
                        "2 tbsp coriander, finely chopped",
                        "15 cashews"
                ),
                Arrays.asList(
                        "For the masala paste: Heat 2 tbsp oil and 2 tbsp butter in a large kadai. Add cloves and cardamom, sautéing on low flame until aromatic.",
                        "Add sliced onions, garlic, and ginger, and sauté until onions soften.",
                        "Add sliced tomatoes and cashews, sauté for 2 minutes, cover, and boil for 10 minutes until the tomatoes soften.",
                        "Cool the mixture, transfer to a blender, and blend to a smooth paste. Pass through a filter to remove seeds and skin. Set the paste aside.",
                        "For the curry: Heat 2 tbsp oil, 2 tbsp butter, bay leaf, and chili in a large kadai. Sauté until aromatic.",
                        "Add finely chopped onion and sauté until golden brown.",
                        "On low flame, add turmeric, chili powder, coriander powder, cumin powder, garam masala, and salt. Sauté until the spices turn aromatic.",
                        "Add the prepared onion-tomato paste and cook until oil separates from the masala.",
                        "Add 1 cup of water, mixing to adjust the consistency.",
                        "Add 2 tbsp cream, mix well, and then add 16 cubes of paneer. Gently mix.",
                        "Simmer for 5 minutes, allowing the paneer to absorb the flavors.",
                        "Add kasuri methi and coriander, mix well.",
                        "Serve Paneer Butter Masala with roti or naan."
                )
        );

        // Recipe 3: Peanut Chutney
        repository.insertRecipe(
                "Peanut Chutney",
                "recipe_peanut_chutney",
                "10 minutes",
                "Medium ~ High",
                Arrays.asList(
                        "Half julienned small onion",
                        "1 small cup peanuts",
                        "2 garlic",
                        "3 ~ 4 finely chopped green chili",
                        "0.5 grams tamarind (1 small petal of tamarind)",
                        "Water",
                        "1 tbsp salt"
                ),
                Arrays.asList(
                        "6 ~ 7 curry leaves",
                        "Half tbsp mustard seeds",
                        "2 dry red chillies",
                        "Half tbsp mustard seeds",
                        "One-fourth tbsp chili powder"
                ),
                Arrays.asList(
                        "Add 2 tbsp of oil to a pan, keep the heat on medium.",
                        "When the oil is hot add garlic, onions to the pan.",
                        "Add peanuts to the pan when onions turn golden brown.",
                        "Fry the ingredients until the peanuts are roasted or you can hear crackling sound of peanuts, turn off the heat and let the ingredients cool down.",
                        "Move the ingredients to a grinder, add turmeric, salt, green chili, dry red chili powder(if tolerant to spice), add  1 small cup of water and grind the ingredients until fine paste(can add more water if required more runny consistency).",
                        "Your chutney mixture is ready(it can be served as it is or add a tempering to it for a crunchy flavour).",
                        "For the tempering heat 1 tbsp of oil in a small sauce pan, add mustard seeds, curry leaves, dry red chili.",
                        "When the mustard seeds starts to crackle add them to the chutney mixture, mix the ingredients.",
                        "Serve the chutney with roti, bread, dosa, idly and enjoy!"
                )
        );

        // Recipe 4: Okra Fry Curry
        repository.insertRecipe(
                "Okra Fry Curry",
                "recipe_okra_fry_curry",
                "30 minutes",
                "Medium",
                Arrays.asList(
                        "Half roughly chopped small onion",
                        "200grams cut okra",
                        "5 ~ 6 chopped garlic",
                        "6 ~ 7 curry leaves",
                        "Half tbsp mustard seeds",
                        "Half tbsp chana dal",
                        "Half tbsp turmeric powder",
                        "Half ~ 1 tbsp dry red chili powder",
                        "1 tbsp salt",
                        "1 tbsp fresh or dry coconut powder"
                ),
                Arrays.asList(
                        "Half tbsp urad dal",
                        "2 finely chopped green chili",
                        "1 tbsp roasted peanuts",
                        "Half tbsp coriander powder"
                ),
                Arrays.asList(
                        "First shallow fry the Okra for 5 ~ 10 minutes on high heat to remove its stickiness. And keep the fried okra a side",
                        "Now, heat a pan with 1 to 2 tbsp of oil on medium heat.",
                        "Add mustard seeds, urad dal, chana dal into the oil.",
                        "When the mustard seeds start crackling, add onions, cut green chili, and curry leaves to the pan.",
                        "Shallow fry the onions for 2 minutes and add shallow fried okra, turmeric to the pan.",
                        "After stirring and mixing all the ingredients add salt, dry red chili powder, coriander powder to the ingredients and mix them.",
                        "When the okra is tender and cooked, switch-off the heat and add 1 tbsp dry coconut powder and roasted peanuts to pan and mix the ingredients.",
                        "Serve the fresh Okra fry curry hot with rice or roti!"
                )
        );

        // Recipe 5: Veg Hakka Noodles
        repository.insertRecipe(
                "Veg Hakka Noodles",
                "recipe_veg_hakka_noodles",
                "25 minutes",
                "Medium",
                Arrays.asList(
                        "1 small cup julienned onions",
                        "1 small cup julienned cabbage",
                        "1 tbsp garlic",
                        "1 tbsp ginger",
                        "1 ~ 2 tbsp soy sauce(light / dark)",
                        "2 packs of noodles(any flour noodles)",
                        "1 tbsp salt",
                        "1 tbsp pepper"
                ),
                Arrays.asList(
                        "2 finely chopped green chili",
                        "1 small cup julienned carrots",
                        "1 small cup julienned bell pepper",
                        "Half tbsp spring onion greens"
                ),
                Arrays.asList(
                        "Boil water in a large bowl for cooking noodles.",
                        "When the water starts boiling add 1 tbsp salt and 2 packs of noodles to it and cook the noodles aldente.",
                        "Remove the noodles from cooking pot and wash with cold water to stop the cooking process and coat the noodles with 1 ~ 2 tbsp of oil. And them aside",
                        "Heat a pan with 3 ~ 4 tbsp of oil on high heat.",
                        "Add ginger, garlic, green chili to the pan and sauté them for 30 seconds.",
                        "Add julienned onions, cabbage, bell pepper, carrots to the pan, add salt and stir fry them on high heat(don't cook until tender just cook for 4 mins on high heat, the vegetables should maintain its crispiness).",
                        "Add cooked noodles, soy sauce, pepper to the pan and mix the ingredients(try not to break the noodles).",
                        "Garnish the noodles with spring onion greens and serve hot!"
                )
        );

        // Recipe 6: Upma
        repository.insertRecipe(
                "Upma",
                "recipe_upma",
                "20 minutes",
                "Low",
                Arrays.asList(
                        "Half roughly chopped small onion",
                        "6 ~ 7 curry leaves",
                        "Half tbsp mustard seeds",
                        "Half tbsp chana dal",
                        "2 ~ 3 finely chopped green chili",
                        "1 roughly chopped tomato",
                        "1 cup semolina",
                        "3 cups water",
                        "1 tbsp salt"
                ),
                Arrays.asList(
                        "1 tbsp peanuts",
                        "Half tbsp urad dal",
                        "1 tbsp finely chopped ginger",
                        "Half finely chopped carrot(1 small carrot)",
                        "4 ~ 5 finely chopped beans",
                        "Half finely chopped potato(1 small potato)",
                        "1 ~ 2 tbsp peas",
                        "Half tbsp finely chopped cilantro"
                ),
                Arrays.asList(
                        "Heat a pan with 1 to 2 tbsp of oil on medium heat.",
                        "Add mustard seeds, chana dal, urad dal, and ginger to the pan.",
                        "When the mustard seeds start to splutter add onions, green chili, curry leaves to the pan and shallow fry them until onions turn translucent.",
                        "Add tomato, carrots, beans, potatoes, peas the pan, reduce the heat to low and close the lid for pan and cooks the vegetables until tender.",
                        "After vegetables are cooked add salt and 3 cups of water to the pan, increase the heat to medium and wait until water starts to boil.",
                        "When water starts to boil add 1 cup of semolina to the pan and continuously stir(for no lumps), until all ingredients are mixed well and semolina absorbs all the water.",
                        "Decrease the heat to low, close the lid and let it cook on low flame for 3 ~ 4 minutes.",
                        "Switch off the heat, add cilantro, mix the ingredients well and serve hot!"
                )
        );

        repository.insertRecipe(
                "Veg Momos",
                "recipe_veg_momos",
                "35 minutes",
                "Medium",
                Arrays.asList(
                        "1½ cup maida / plain flour",
                        "water for kneading",
                        "4 tsp oil",
                        "3 clove garlic, finely chopped",
                        "1 inch ginger, finely chopped",
                        "2 chilli, finely chopped",
                        "1 cup carrot, grated",
                        "2 cup cabbage, shredded",
                        "½ tsp pepper, crushed",
                        "1 tsp salt"
                ),
                Arrays.asList(
                        "4 tbsp spring onion"
                ),
                Arrays.asList(
                        "Firstly, in a large mixing bowl take 1½ cup maida and ½ tsp salt.",
                        "Add water and knead dough for at least 5 minutes.",
                        "Knead to a smooth and soft dough adding water as required.",
                        "Grease the dough with oil and rest for 30 minutes.",
                        "Meanwhile, prepare stuffing by heating 3 tsp oil and saute 3 clove garlic, 1 inch ginger and 2 chilli.",
                        "Also, add 2 tbsp spring onion and saute on high flame.",
                        "Further, add 1 cup carrot and 2 cup cabbage. stir fry on high flame.",
                        "Now add ½ tsp pepper and ½ tsp salt",
                        "Stir-fry without overcooking vegetables.",
                        "Additionally, 2 tbsp spring onion and stuffing mixture is ready.",
                        "After 30 minutes, take the prepared momos dough and knead well again for a minute.",
                        "Further, pinch a small ball and flatten.",
                        "Now dust with some maida and start to roll using a rolling pin.",
                        "Now place a heaped tbsp of prepared stuffing in the centre.",
                        "Start pleating the edges slowly and gather everything.",
                        "Press in middle and seal the momos forming a bundle.",
                        "Heat a steamer and arrange the momos in the tray without touching each other.",
                        "Furthermore, steam momos for 10-12 minutes or till shiny sheen appears over it.",
                        "Finally, VEG MOMOS RECIPE is ready to enjoy with momos."
                )
        );

        // New Recipe: Momos Chutney
        repository.insertRecipe(
                "Momos Chutney",
                "recipe_momos_chutney",
                "12 minutes",
                "Medium",
                Arrays.asList(
                        "2 cup water",
                        "3 tomato, ripened",
                        "4 dried red chilli",
                        "4 clove garlic",
                        "1 inch ginger",
                        "1 tsp vinegar",
                        "1 tsp soy sauce"
                ),
                Arrays.asList(
                        "5 almonds / badam, blanched"
                ),
                Arrays.asList(
                        "Firstly, in a deep bottom pan, take 2 cup water and add 3 tomato, 4 dried red chilli.\n",
                        "Boil for 5 minutes or till tomatoes start peeling its skin.",
                        "Cool down the blanched tomatoes and chilli.",
                        "Once it is cooled down, remove the skin of tomatoes.",
                        "Transfer the peeled tomatoes and red chilli into blender.",
                        "Also add 4 clove garlic, 1 inch ginger.",
                        "Further, add 5 almonds to have a good thickness to sauce.",
                        "Additionally add 1 tsp sugar, 1 tsp vinegar, ¼ tsp salt and 1 tsp soy sauce.",
                        "Blend to smooth paste without adding water.",
                        "Finally, momos chutney is ready to enjoy with wheat momos or veg momos."
                )
        );

        // New Recipe: Cream of Spinach Soup
        repository.insertRecipe(
                "Spinach Soup",
                "recipe_spinach_soup",
                "15 minutes",
                "Low",
                Arrays.asList(
                        "4 tsp oil",
                        "2 cloves garlic",
                        "½ inch ginger",
                        "2 tbsp onion, sliced",
                        "1 bunch spinach",
                        "1 tbsp butter",
                        "2 tsp maida / All-purpose flour",
                        "1 bay leaf",
                        "4 cup milk"
                ),
                Arrays.asList(
                        "2 tbsp cream"
                ),
                Arrays.asList(
                        "firstly, in a pan heat 2 tsp oil. add 2 cloves garlic, ½ inch ginger and 2 tbsp onion.",
                        "Saute until the onions shrink slightly. Do not brown the onions as the flavour will change.",
                        "now add 1 bunch spinach and saute until the spinach shrinks completely.",
                        "Cool completely, and transfer to mixer jar.",
                        "add water and grind to a smooth paste. keep aside.",
                        "In a large pan/pot heat 2 tsp oil, 1 tbsp butter and add 2 tsp maida.",
                        "Saute on low flame until the maida turns aromatic. Do not brown the maida, just fry until the maida turns aromatic.",
                        "Now add 1 bay leaf and saute slightly.",
                        "Further, add 4 cup of milk and mix using a whisk",
                        "Continue to cook until the milk thickens.",
                        "Also, add in prepared spinach puree and cook well. simmer for 2 minutes, or until the flavors are absorbed.",
                        "12. add in ½ tsp pepper powder, ½ tsp salt, ½ tsp sugar, and 2 tbsp cream. mix well.",
                        "finally, enjoy spinach soup recipe garnished with cream."
                )
        );

        // New Recipe: Corn Rice
        repository.insertRecipe(
                "Corn Rice",
                "recipe_corn_rice",
                "35 minutes",
                "Medium",
                Arrays.asList(
                        "2 tbsp oil",
                        "5 cloves garlic, sliced",
                        "4 chilli, slit",
                        "1 onion, sliced",
                        "1 tsp ginger paste",
                        "½ Bell-pepper, chopped",
                        "1½ cup sweet corn",
                        "2 cup water",
                        "1 cup basmati rice, soaked",
                        "2 tsp lemon juice"
                ),
                Arrays.asList(
                        "2 tbsp coriander, chopped"
                ),
                Arrays.asList(
                        "Firstly, in a large pan/pot heat 2 tbsp oil. add 2 bay leaf, 1 tsp cumin, 1 black cardamom, 1 inch cinnamon, 3 cardamom, 7 cloves, and saute until the spices turn aromatic.",
                        "Add 5 cloves garlic, 4 chilli and saute slightly.",
                        "Also add 1 onion, 1 tsp ginger paste and saute until the onion shrinks slightly.",
                        "Further add ½ bell-pepper, 1½ cup sweet corn, ½ tsp garam masala, and 1 tsp salt.",
                        "Saute for a minute or until the corn turns aromatic.",
                        "Pour in 2 cup water and get to a boil.",
                        "Once the water comes to a boil, add 1 cup basmati rice, and 2 tsp lemon juice and mix well.",
                        "Cover and simmer for 20 minutes, or until the rice is cooked well.",
                        "Finally, garnish with 2 tbsp coriander, and corn rice recipe is ready to enjoy."
                )
        );

        // New Recipe: Fried Rice
        repository.insertRecipe(
                "Fried Rice",
                "recipe_fried_rice",
                "30 minutes",
                "Medium",
                Arrays.asList(
                        "1 cup basmati rice",
                        "3 tsp oil",
                        "water for soaking & boiling",
                        "2 clove garlic, finely chopped",
                        "½ onion, finely chopped",
                        "¼ carrot, finely chopped",
                        "5 beans, chopped",
                        "¼ capsicum, finely chopped",
                        "2 tbsp soy sauce",
                        "1 tbsp vinegar"
                ),
                Arrays.asList(
                        "4 tbsp spring onion, chopped",
                        "2 tbsp peas / matar",
                        "2 tbsp cabbage, finely chopped"
                ),
                Arrays.asList(
                        "Firstly, in a large pan/wok heat 2 tbsp oil and stir-fry 2 clove garlic.",
                        "Also, saute ½ onion and 2 tbsp spring onion until they sweat.",
                        "Furthermore add vegetables of your choice like ¼ carrot, 2 tbsp cabbage, 2 tbsp peas, 5 beans, ¼ capsicum and ½ tbsp salt.",
                        "Stir fry on high flame without overcooking vegetables.",
                        "Now add 2 tbsp soy sauce and 1 tbsp vinegar. stir-fry until the sauce is combined well.",
                        "Keeping the flame on high, add cooked rice.",
                        "Also, add 1 tsp pepper and ¼ tsp salt. adjust the salt as soy sauce has salt.",
                        "Stir-fry by mixing well making sure rice grains won't break.",
                        "Further, add 2 tbsp spring onions and mix well.",
                        "Finally, enjoy veg fried rice."
                )
        );
    }
}
