/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uzduotis2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Application; 
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 

import javafx.scene.Scene; 
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button; 
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane; 
import javafx.scene.text.Text; 
import javafx.scene.control.TextField; 
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
/**
 *
 * @author Martynas Zabulionis 3 grupė
 * 
 */


public class Uzduotis2 extends Application
{
    public static double PROCENT = 6.0 / 12.0 / 100.0;
    public static double round(double value, int places)
    {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}
    public static abstract class PaskolosTipas
    {
	String men_nr;
	double suma;
	double suma2;
	double palūkanos;
	double liko_grąžinti;
	
	public String getMėnNr() { return men_nr; }
	public double getSuma() { return suma; }
	public double getSuma2() { return suma2; }
	public double getPalūkanos() { return palūkanos; }
	public double getLikoGrąžinti() { return liko_grąžinti; }
        
	
        private PaskolosTipas(int month_nm, double all_mn, int months)
        {
            if (month_nm % 12 == 1)
		this.men_nr = "" + month_nm + "\t(" + (month_nm + 12) / 12 + " metai)";
	    else
		this.men_nr = "" + month_nm;
        }
	/**
	* Apskaičiuoja sumą, kurią reikia sumokėti tą mėnesį.
	*
	* @param  all_mn  visi pinigai
	* @param  months mėnesių skaičius
	* @return      grąžina sumą, kurią reikia sumokėti tą mėnesį
	*/
        public abstract double SkaičiuotiSumą(double all_mn, int months);
    }
    public static class Anuitetas extends PaskolosTipas
    {
	private Anuitetas(int month_nm, double all_mn, int months)
	{
            super(month_nm, all_mn, months);
            this.suma = SkaičiuotiSumą(all_mn, months);
            this.suma2 = round(all_mn / months, 2);
	    this.palūkanos = round(suma - suma2, 2);
	    this.liko_grąžinti = round(all_mn - suma2 * month_nm, 2);
	}
	/**
	* Apskaičiuoja sumą, kurią reikia sumokėti tą mėnesį.
	*
	* @param  all_mn  visi pinigai
	* @param  months mėnesių skaičius
	* @return      grąžina sumą, kurią reikia sumokėti tą mėnesį
	*/
        @Override
	public double SkaičiuotiSumą(double all_mn, int months)
        {
            return round(all_mn * ((PROCENT * Math.pow(PROCENT + 1, months)) / (Math.pow(PROCENT + 1, months) - 1)), 2);
        }
    }
    public static class Linijinis extends PaskolosTipas
    {
        static double likutis;
	private Linijinis(int month_nm, double all_mn, int months)
	{
            super(month_nm, all_mn, months);
            this.suma = SkaičiuotiSumą(all_mn, months);   
	    this.suma2 = round(all_mn / months, 2);
	    this.palūkanos = round(suma - suma2, 2);
	    this.liko_grąžinti = round(all_mn - suma2 * month_nm, 2);
	}
	/**
	* Apskaičiuoja sumą, kurią reikia sumokėti tą mėnesį.
	*
	* @param  all_mn  visi pinigai
	* @param  months mėnesių skaičius
	* @return      grąžina sumą, kurią reikia sumokėti tą mėnesį
	*/
        @Override
	public double SkaičiuotiSumą(double all_mn, int months)
        {
            double i = round(likutis * PROCENT + (all_mn / months), 2);
            likutis -= i;
            return i;
        }
    }
    
    @Override
   public void start(Stage stage)
   {      
      //creating label email 
      Text t_paskolos_suma = new Text("Paskolos suma:");
      Text t_eur = new Text("€");
      Text t_paskolos_terminas = new Text("Paskolos terminas:"); 
      Text t_metai = new Text("Metai:");
      Text t_menesiai = new Text("Mėnesiai:");
      Text t_grazinimo_grafikas = new Text("Grąžinimo metodas:");
      
      //Creating Text Filed for email        
      TextField f_paskolos_suma = new TextField();
      
      Pattern pattern = Pattern.compile("\\d{0,9}|\\d{1,9}(\\,|\\.)\\d{0,2}");
      TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern.matcher(change.getControlNewText()).matches() ? change : null;});

      f_paskolos_suma.setTextFormatter(formatter);
     
      
      TextField f_metai = new TextField();
      
      Pattern pattern2 = Pattern.compile("[1-3]?[1-9]?|[1-4]0|0?");
      TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern2.matcher(change.getControlNewText()).matches() ? change : null;});

      f_metai.setTextFormatter(formatter2);
      
      TextField f_menesiai = new TextField();
      
      Pattern pattern3 = Pattern.compile("[2-9]|1?[0-1]|0?");
      TextFormatter formatter3 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern3.matcher(change.getControlNewText()).matches() ? change : null;});

      f_menesiai.setTextFormatter(formatter3);
      
      final ToggleGroup group = new ToggleGroup();
      
      RadioButton rb1 = new RadioButton("Linijinis");
      rb1.setToggleGroup(group);
      rb1.setSelected(true);
      
      RadioButton rb2 = new RadioButton("Anuiteto");
      rb2.setToggleGroup(group);
 
      //Creating Buttons 
      Button b_skaiciuoti = new Button("Skaičiuoti");
     
      b_skaiciuoti.setOnAction(new EventHandler<ActionEvent>()
      {
            @Override
            public void handle(ActionEvent e) 
            {
                String paskolos_suma = f_paskolos_suma.getText().replaceAll(",", ".");
                int len = paskolos_suma.length();
                if (len == 0 || f_metai.getText().length()  == 0 || f_menesiai.getText().length() == 0)
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Klaida");
                    alert.setHeaderText(null);
                    alert.setContentText("Yra tuščių laukų!");

                    alert.showAndWait();
                }
                else if (paskolos_suma.charAt(len - 1) == ',' || paskolos_suma.charAt(0) == '0')
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Klaida");
                    alert.setHeaderText(null);
                    alert.setContentText("Blogas skaičiaus formatas paskolos sumos langelyje!");

                    alert.showAndWait();
                }
                else if (Integer.parseInt(f_metai.getText()) + Integer.parseInt(f_menesiai.getText()) == 0)
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Klaida");
                    alert.setHeaderText(null);
                    alert.setContentText("Laiko tarpas negali būti 0!");

                    alert.showAndWait();
                }
		else
		{
		    ObservableList<PaskolosTipas> data = FXCollections.observableArrayList();
		    TableView<PaskolosTipas> table = new TableView<PaskolosTipas>();
		   
		    
		    table.setEditable(true);
 
		    TableColumn mėn_nr = new TableColumn("Mėn. nr.");
		    TableColumn suma = new TableColumn("Suma (€)");
		    TableColumn suma2 = new TableColumn("Išskirta suma");
		    TableColumn liko_grąžinti = new TableColumn("Liko grąžinti (€)");
		   
		    TableColumn suma3 = new TableColumn("Suma (€)");
		    TableColumn palūkanos = new TableColumn("Palūkanos (€)");
		    
		    
		    mėn_nr.setCellValueFactory(new PropertyValueFactory<>("mėnNr"));
		    suma.setCellValueFactory(new PropertyValueFactory<>("suma"));
		    suma3.setCellValueFactory(new PropertyValueFactory<>("suma2"));
		    palūkanos.setCellValueFactory(new PropertyValueFactory<>("palūkanos"));
		    liko_grąžinti.setCellValueFactory(new PropertyValueFactory<>("likoGrąžinti"));
		    
		    mėn_nr.prefWidthProperty().bind(table.widthProperty().divide(5));
		    suma.prefWidthProperty().bind(table.widthProperty().divide(5));
		    suma3.prefWidthProperty().bind(table.widthProperty().divide(5));
		    palūkanos.prefWidthProperty().bind(table.widthProperty().divide(5));
		    liko_grąžinti.prefWidthProperty().bind(table.widthProperty().divide(5));
		    
		    suma2.getColumns().addAll(suma3, palūkanos);
		    table.getColumns().addAll(mėn_nr, suma, suma2, liko_grąžinti);
		    
		    
		    
		    int laikas = Integer.parseInt(f_menesiai.getText()) + 12 * Integer.parseInt(f_metai.getText());
                    
                    if(rb1.isSelected() == true)
                    {
                        Linijinis.likutis = Double.parseDouble(paskolos_suma);
                        for (int i = 1; i <= laikas; ++i)
                        {
                            data.add(new Linijinis(i, Double.parseDouble(paskolos_suma), laikas));
                        }
                        Linijinis.likutis = 0;
                    }
                    else
                    {
                        for (int i = 1; i <= laikas; ++i)
                        {
                            data.add(new Anuitetas(i, Double.parseDouble(paskolos_suma), laikas));
                        }
                    }
		    table.setItems(data);
		    
                    
                    GridPane gridPane = new GridPane();
		    GridPane gridPane2 = new GridPane();
		    gridPane.add(table, 0, 0);
                    gridPane.setPadding(new Insets(30, 30, 30, 30)); 
                    gridPane.setVgap(20); 
                    gridPane.setHgap(5);
                    //gridPane.setAlignment(Pos.CENTER);
                    gridPane.setStyle("-fx-background-color: BEIGE;");
                    
                    Button b_ataskaita = new Button("Gauti ataskaitą faile");
                    Button b_periodas = new Button("Rodyti periodą");
                    //b_periodas.setMinWidth(100);
                    b_ataskaita.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
                    gridPane.add(b_ataskaita, 0, 2);
                    
                    b_periodas.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
                    gridPane2.add(b_periodas, 0, 0);
                    
                    Text t_nuo = new Text("Nuo (mėn. nr.): ");
                    TextField f_nuo = new TextField();
                    
		    Pattern pattern4 = Pattern.compile("([1-9]?[1-9]{0,2})|([1-9]\\d{0,2})");
		    TextFormatter formatter4 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
		    return pattern4.matcher(change.getControlNewText()).matches() ? change : null;});

		    f_nuo.setTextFormatter(formatter4);
      
      
                    Text t_iki = new Text("Iki (mėn. nr.): ");
                    TextField f_iki = new TextField();
		    
		    TextFormatter formatter5 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
		    return pattern4.matcher(change.getControlNewText()).matches() ? change : null;});

                    f_iki.setTextFormatter(formatter5);
                    gridPane2.add(t_nuo, 3, 0);
                    gridPane2.add(f_nuo, 4, 0);
                    gridPane2.add(t_iki, 15, 0);
                    gridPane2.add(f_iki, 16, 0);
		    gridPane2.setHgap(5);
		    gridPane.add(gridPane2, 0, 1);
                    
                    
                    GridPane.setHgrow(table, Priority.ALWAYS);
                    GridPane.setVgrow(table, Priority.ALWAYS);
		    b_periodas.setOnAction(new EventHandler<ActionEvent>()
                    {
                        @Override
                        public void handle(ActionEvent e) 
                        {
			    if (f_nuo.getText().length() == 0 || f_iki.getText().length() == 0)
			    {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Klaida");
				alert.setHeaderText(null);
				alert.setContentText("Yra tuščių langelių!");
				alert.showAndWait();
			    }
			    else
			    {
				int nuo = Integer.parseInt(f_nuo.getText());
				int iki = Integer.parseInt(f_iki.getText());

				if (iki > laikas)
				{
				    iki = laikas;
				    f_iki.setText("" + laikas);
				}			    
				if (nuo > iki)
				{
				    nuo = iki;
				    f_nuo.setText("" + iki);
				}
				ObservableList<PaskolosTipas> data2 = FXCollections.observableArrayList(data);
				if (iki < laikas)
				    data2.remove(iki, laikas);
								
				if (nuo > 1)
				    data2.remove(0, nuo - 1);
				
				table.setItems(data2);
			    }
			}
		    });
		    
                    b_ataskaita.setOnAction(new EventHandler<ActionEvent>()
                    {
                        @Override
                        public void handle(ActionEvent e) 
                        {
                            BufferedWriter bw = null;
                            FileWriter fw = null;
                            try
                            {
                                    fw = new FileWriter("ataskaita.txt");
                                    bw = new BufferedWriter(fw);
                                    
                                    bw.write("Mėn. nr.\tPilna suma (€)\t\tSuma(€)\t\tPalūkanos (€)\t\tLiko grąžinti (€)");

				    int max = table.getItems().size();
                                    for (int a = 0; a < max; ++a)
                                    {
                                        bw.newLine();
					bw.write("" + mėn_nr.getCellObservableValue(a).getValue() + "\t"
						    + suma.getCellObservableValue(a).getValue() + "\t\t"
						    + suma3.getCellObservableValue(a).getValue() + "\t\t"
						    + palūkanos.getCellObservableValue(a).getValue() + "\t\t"
						    + liko_grąžinti.getCellObservableValue(a).getValue());
					
                                    }
                                    Alert alert = new Alert(AlertType.CONFIRMATION);
                                    alert.setTitle("Padaryta");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Duomenys išsaugoti ataskaita.txt faile.");

                                    alert.showAndWait();

                            } catch (IOException m) {
                                    m.printStackTrace();

                            } finally {
                                    try {
                                            if (bw != null)
                                                    bw.close();

                                            if (fw != null)
                                                    fw.close();

                                    } catch (IOException ex) {
                                            ex.printStackTrace();
                                    }
                            }
                        }
                    });
		    //GridPane gridPane = new GridPane();
		    Stage stage = new Stage();
		    
		    Scene scene = new Scene(gridPane); 
       
		    //Setting title to the Stage 
		    stage.setTitle("Suskaičiuota"); 

		    stage.setScene(scene);

		    stage.setWidth(750);
		    stage.setHeight(800);
	
		    //Displaying the contents of the stage 
		    stage.show(); 
		}
            }
      });
      
      //Creating a Grid Pane 
      GridPane gridPane = new GridPane();
      GridPane gridPane1 = new GridPane();
      GridPane gridPane2 = new GridPane();
      GridPane gridPane3 = new GridPane();

      gridPane1.setHgap(5); 
      gridPane1.add(t_paskolos_suma, 0, 0); 
      gridPane1.add(f_paskolos_suma, 1, 0);
      gridPane1.add(t_eur, 2, 0); 
      
      
      gridPane2.setHgap(5); 
      gridPane2.add(t_metai, 0, 0); 
      gridPane2.add(f_metai, 1, 0); 
      
      gridPane3.setHgap(5); 
      gridPane3.add(t_menesiai, 0, 0);
      gridPane3.add(f_menesiai, 1, 0);
      
      //Setting size for the pane 
      //gridPane.setMinSize(400, 200); 
      
      //Setting the padding  
      gridPane.setPadding(new Insets(30, 60, 30, 60)); 
      
      //Setting the vertical and horizontal gaps between the columns 
      gridPane.setVgap(20); 
      gridPane.setHgap(5);       
      
      //Setting the Grid alignment 
      gridPane.setAlignment(Pos.CENTER); 
       
      //Arranging all the nodes in the grid 
      gridPane.add(gridPane1, 0, 0); 
      gridPane.add(t_paskolos_terminas, 0, 2);       
      gridPane.add(gridPane2, 0, 3); 
      gridPane.add(gridPane3, 1, 3);
      gridPane.add(t_grazinimo_grafikas, 0, 5);
      gridPane.add(rb1, 0, 6);
      gridPane.add(rb2, 1, 6);
      gridPane.add(b_skaiciuoti, 0, 8);
      //gridPane2.setHalignment(b_skaiciuoti, HPos.RIGHT);
       
      //Styling nodes  
      b_skaiciuoti.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
       
      t_paskolos_suma.setStyle("-fx-font: normal bold 20px 'serif' ");
      t_eur.setStyle("-fx-font: normal bold 20px 'serif' ");
      t_paskolos_terminas.setStyle("-fx-font: normal bold 20px 'serif' ");
      t_grazinimo_grafikas.setStyle("-fx-font: normal bold 20px 'serif' ");
      gridPane.setStyle("-fx-background-color: BEIGE;"); 
       
      //Creating a scene object 
      Scene scene = new Scene(gridPane); 
       
      //Setting title to the Stage 
      stage.setTitle("Paskolos skaičiuoklė"); 
         
      //Adding scene to the stage 
      stage.setScene(scene);
      
      //Displaying the contents of the stage 
      stage.show(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}