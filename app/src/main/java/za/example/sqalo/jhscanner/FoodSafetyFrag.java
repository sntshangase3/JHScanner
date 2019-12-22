package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by sibusison on 2017/07/30.
 */
public class FoodSafetyFrag extends Fragment  {

    View rootView;
TextView txtBest_Before_Date,txtSell_By_Date,txtExpiry_By_Date,txtSalmonella_species,txtCampylobacter,txtEscherichia,txtStaph_aureus ,txtListeria,txtClostridium_perfringens,txtNoroviruses;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.foodsafety, container, false);
        txtBest_Before_Date=(TextView)rootView.findViewById(R.id.txtBest_Before_Date);
        txtSell_By_Date=(TextView)rootView.findViewById(R.id.txtSell_By_Date);
        txtExpiry_By_Date=(TextView)rootView.findViewById(R.id.txtExpiry_By_Date);
        txtSalmonella_species=(TextView)rootView.findViewById(R.id.txtSalmonella_species);
        txtCampylobacter=(TextView)rootView.findViewById(R.id.txtCampylobacter);
        txtEscherichia=(TextView)rootView.findViewById(R.id.txtEscherichia);
        txtStaph_aureus=(TextView)rootView.findViewById(R.id.txtStaph_aureus);
        txtListeria=(TextView)rootView.findViewById(R.id.txtListeria);
        txtClostridium_perfringens=(TextView)rootView.findViewById(R.id.txtClostridium_perfringens);
        txtNoroviruses=(TextView)rootView.findViewById(R.id.txtNoroviruses);

        txtBest_Before_Date.setText(Html.fromHtml("The <b><u>“Best Before Date”</u></b> is, according to the manufacturers indicates that the quality of their food might begin to diminish after that date – e.g. product: may just lose its freshness, taste, aroma or nutrients - but it is still good to use and the shelf life is still active for a period beyond that of a pre-determined best-by date. "));
        txtSell_By_Date.setText(Html.fromHtml("The <b><u>“Sell By Date”</u></b> on a product is the items date, the end of its shelf life at the store. This is the last date stores are supposed to display the product for sale, after the Sell By Date the stores should remove the product, the store Shelf Life has expired. You can buy the product before the sell-by date expires and you can still store it at home for some time beyond that date, as long as you follow safe storage procedures."));
        txtExpiry_By_Date.setText(Html.fromHtml("The <b><u>“Expiry By Date”</u></b> on a product is the items Expiration date that tells consumers the last day a product is safe to consume. A food should never be consumed after the expiry date. Date marks give a guide to quality and safety of products and after this date product quality will be deemed to have deteriorated or may become unsafe to eat."));
        txtSalmonella_species.setText(Html.fromHtml("<b><u>Salmonella species</u></b>. This is the bacterium that can cause illness when you eat raw or undercooked eggs (even in chocolate chip cookie dough!). They are notoriously known for causing more deaths compare to other food-borne pathogen. Salmonella infection can lead to fever, abdominal cramps, and diarrhea within 12 hours to three days after eating the contaminated food."));
        txtCampylobacter.setText(Html.fromHtml("<b><u>Campylobacter</u></b>. This is the most common cause of diarrhea and abdominal cramps from food-related illness. While most raw poultry meat has campylobacter on it, vegetables and fruits can also become contaminated with juices that drip from raw chicken. Unpasteurized milk or cheese or contaminated water may also cause this infection."));
        txtEscherichia.setText(Html.fromHtml("<b><u>Escherichia coli 0157:H7 (E. coli)</u></b>. This is a common cause of dehydrating diarrhea worldwide. While most strains of E. coli live in the intestines of healthy humans and animals, the 0157:H7 strain can be deadly, leading to bloody diarrhea and even kidney failure. Other, less dangerous, E. coli are responsible for most cases of travelers diarrhea. Although most types of E. coli are harmless, some types can make you sick. The worst type of E. coli, known as E. coli O157:H7, causes bloody diarrhea and can sometimes cause kidney failure and even death. E. coli O157:H7 makes a toxin called Shiga toxin and is known as a Shiga toxin-producing E. coli (STEC).  There are many other types of STEC, and some can make you just as sick as E. coli O157:H7. One severe complication associated with E. coli infection is hemolytic uremic syndrome (HUS). The infection produces toxic substances that destroy red blood cells, causing kidney injury. HUS can require intensive care, kidney dialysis, and transfusions."));
        txtStaph_aureus.setText(Html.fromHtml("<b><u>Staph aureus</u></b>. This organism contaminates many different kinds of food. It causes food poisoning with vomiting followed by diarrhea in many cases. It is often associated with restaurants or picnics where food is not properly refrigerated or stays out of the refrigerator too long."));
        txtListeria.setText(Html.fromHtml("<b><u>Listeria</u></b> is the name of a bacteria found in soil and water and some animals, including poultry and cattle. It can be present in raw milk and foods made from raw milk. It can also live in food processing plants and contaminate a variety of processed meats. Listeria is unlike many other germs because it can grow even in the cold temperature of the refrigerator. Listeria is killed by cooking and pasteurization. Like the famous mouthwash Listerine, Listeria monocytogenes was named after antiseptic pioneer Joseph Lister. It's a very common bacterium with an unusual trait: It can survive for years."));
        txtClostridium_perfringens.setText(Html.fromHtml("<b><u>Clostridium perfringens (C. perfringens)</u></b> is one of the most common causes of food poisoning in the United States. According to some estimates, this type of bacteria causes nearly a million illnesses each year. Cooking kills the growing C. perfringens cells that cause food poisoning, but not necessarily the spores that can grow into new cells. If cooked food is not promptly served or refrigerated, the spores can grow and produce new cells. These bacteria thrive between 40-140˚F (the “Danger Zone”). This means that they grow quickly at room temperature, but they cannot grow at refrigerator or freezer temperatures. C. perfringens infections often occur when foods are prepared in large quantities and are then kept warm for a long time before serving. That’s why outbreaks of these infections are usually linked to institutions (such as hospitals, school cafeterias, prisons, and nursing homes) or events with catered food."));
        txtNoroviruses.setText(Html.fromHtml("<b><u>Noroviruses</u></b> are the most common cause of acute gastroenteritis (infection of the stomach and intestines). Norovirus illness spreads easily and is often called stomach flu or viral gastroenteritis, People who are infected can spread it directly to other people, or can contaminate food or drinks they prepare for other people. The virus can also survive on surfaces that have been contaminated with the virus or be spread through contact with an infected person."));

          return rootView;
    }



}


