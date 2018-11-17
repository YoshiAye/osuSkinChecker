package me.roan.osuskinchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import me.roan.osuskinchecker.SkinIni.ManiaIni.Column;

public class SkinIni{
	private boolean usedDefault = false;
	private Map<String, List<String>> comments = new HashMap<String, List<String>>();
	private String last = null;
	
	//general
	protected final Setting<String> name = new Setting<String>("-");
	protected final Setting<String> author = new Setting<String>("-");
	protected final Setting<Version> version = new Setting<Version>(Version.LATEST);
	protected final Setting<Boolean> cursorExpand = new Setting<Boolean>(true);
	protected final Setting<Boolean> cursorCentre = new Setting<Boolean>(true);
	protected final Setting<Boolean> cursorRotate = new Setting<Boolean>(true);
	protected final Setting<Boolean> cursorTrailRotate = new Setting<Boolean>(true);
	protected final Setting<Integer> animationFramerate = new Setting<Integer>();//non negative

	//combo bursts
	protected final Setting<Boolean> layeredHitSounds = new Setting<Boolean>(true);
	protected final Setting<Boolean> comboBurstRandom = new Setting<Boolean>(false);
	protected final Setting<String> customComboBurstSounds = new Setting<String>();//list of ints, positive values only

	//standard
	protected final Setting<Boolean> hitCircleOverlayAboveNumber = new Setting<Boolean>(true);
	protected final Setting<SliderStyle> sliderStyle = new Setting<SliderStyle>(2);//1 or 2
	protected final Setting<Boolean> sliderBallFlip = new Setting<Boolean>(false);
	protected final Setting<Boolean> allowSliderBallTint = new Setting<Boolean>(false);
	protected final Setting<Boolean> spinnerNoBlink = new Setting<Boolean>(false);
	protected final Setting<Boolean> spinnerFadePlayfield = new Setting<Boolean>(false);
	protected final Setting<Boolean> spinnerFrequencyModulate = new Setting<Boolean>(true);

	//colours
	protected final Setting<Colour> songSelectActiveText = new Setting<Colour>(new Colour(0, 0, 0));
	protected final Setting<Colour> songSelectInactiveText = new Setting<Colour>(new Colour(255, 255, 255));
	protected final Setting<Colour> menuGlow = new Setting<Colour>(new Colour(0, 78, 155));
	
	protected final Setting<Colour> starBreakAdditive = new Setting<Colour>(new Colour(255, 182, 193));
	protected final Setting<Colour> inputOverlayText = new Setting<Colour>(new Colour(0, 0, 0));
	
	protected final Setting<Colour> sliderBall = new Setting<Colour>(new Colour(2, 170, 255));
	protected final Setting<Colour> sliderTrackOverride = new Setting<Colour>();
	protected final Setting<Colour> sliderBorder = new Setting<Colour>(new Colour(0, 0, 0));
	protected final Setting<Colour> spinnerBackground = new Setting<Colour>(new Colour(100, 100, 100));
	
	protected final Setting<Colour> combo1 = new Setting<Colour>(new Colour(255, 192, 0));
	protected final Setting<Colour> combo2 = new Setting<Colour>();
	protected final Setting<Colour> combo3 = new Setting<Colour>();
	protected final Setting<Colour> combo4 = new Setting<Colour>();
	protected final Setting<Colour> combo5 = new Setting<Colour>();
	protected final Setting<Colour> combo6 = new Setting<Colour>();
	protected final Setting<Colour> combo7 = new Setting<Colour>();
	protected final Setting<Colour> combo8 = new Setting<Colour>();

	//fonts
	protected final Setting<String> hitCirclePrefix = new Setting<String>("default");
	protected final Setting<Integer> hitCircleOverlap = new Setting<Integer>(-2);//negative allowed

	protected final Setting<String> scorePrefix = new Setting<String>("score");
	protected final Setting<Integer> scoreOverlap = new Setting<Integer>(-2);//negative allowed

	protected final Setting<String> comboPrefix = new Setting<String>("score");
	protected final Setting<Integer> comboOverlap = new Setting<Integer>(-2);//negative allowed

	//ctb
	protected final Setting<Colour> hyperDash = new Setting<Colour>(new Colour(255, 0, 0));
	protected final Setting<Colour> hyperDashFruit = new Setting<Colour>();
	protected final Setting<Colour> hyperDashAfterImage = new Setting<Colour>();

	protected final ManiaIni[] mania = new ManiaIni[ManiaIni.MAX_KEYS];

	public final void createManiaConfiguration(int keys){
		mania[keys - 1] = new ManiaIni(keys);
	}

	protected static final class ManiaIni{
		protected static final int MAX_KEYS = 10;
		protected int keys;//non negative
		protected final Setting<Double> columnStart = new Setting<Double>(136.0D);
		protected final Setting<Double> columnRight = new Setting<Double>(19.0D);
		protected final Setting<double[]> columnSpacing = new Setting<double[]>();//n-1 numbers
		protected final Setting<double[]> columnWidth = new Setting<double[]>();//n numbers
		protected final Setting<double[]> columnLineWidth = new Setting<double[]>();//n+1 numbers
		protected final Setting<Double> barlineHeight = new Setting<Double>(1.2D);
		protected final Setting<double[]> lightingNWidth = new Setting<double[]>();//n numbers
		protected final Setting<double[]> lightingLWidth = new Setting<double[]>();//n numbers
		protected final Setting<Double> widthForNoteHeightScale = new Setting<Double>(-1.0D);
		protected final Setting<Integer> hitPosition = new Setting<Integer>(402);
		protected final Setting<Integer> lightPosition = new Setting<Integer>(413);
		protected final Setting<Integer> scorePosition = new Setting<Integer>(325);
		protected final Setting<Integer> comboPosition = new Setting<Integer>(111);
		protected final Setting<Boolean> judgementLine = new Setting<Boolean>(true);
		protected final Setting<SpecialStyle> specialStyle = 0;//0, 1 or 2
		protected final Setting<ComboBurstStyle> comboBurstStyle = 1;//0, 1 or 2
		protected final Setting<Boolean> splitStages = new Setting<Boolean>();
		protected final Setting<Double> stageSeparation = new Setting<Double>(40.0D);
		protected final Setting<Boolean> separateScore = new Setting<Boolean>(true);
		protected final Setting<Boolean> keysUnderNotes = new Setting<Boolean>(false);
		protected final Setting<Boolean> upsideDown = new Setting<Boolean>(false);
		protected final Setting<Boolean> keyFlipWhenUpsideDown = new Setting<Boolean>(true);
		protected final Setting<Boolean> noteFlipWhenUpsideDown = new Setting<Boolean>(true);
		protected final Setting<NoteBodyStyle> noteBodyStyle = 1;//0, 1, 2
		protected final Setting<Colour> colourColumnLine = new Setting<Colour>(new Colour(255, 255, 255, 255));
		protected final Setting<Colour> colourBarline = new Setting<Colour>(new Colour(255, 255, 255, 255));
		protected final Setting<Colour> colourJudgementLine = new Setting<Colour>(new Colour(255, 255, 255));
		protected final Setting<Colour> colourKeyWarning = new Setting<Colour>(new Colour(0, 0, 0));
		protected final Setting<Colour> colourHold = new Setting<Colour>(new Colour(255, 191, 51, 255));
		protected final Setting<Colour> colourBreak = new Setting<Colour>(new Colour(255, 0, 0));

		protected Column[] columns;

		protected final Setting<String> stageLeft = new Setting<String>("mania-stage-left");
		protected final Setting<String> stageRight = new Setting<String>("mania-stage-right");
		protected final Setting<String> stageBottom = new Setting<String>("mania-stage-bottom");
		protected final Setting<String> stageHint = new Setting<String>("mania-stage-hint");
		protected final Setting<String> stageLight = new Setting<String>("mania-stage-light");
		protected final Setting<String> lightingN = new Setting<String>("LightingN");
		protected final Setting<String> lightingL = new Setting<String>("LightingL");
		protected final Setting<String> warningArrow = new Setting<String>("mania-warningarrow");

		protected final Setting<String> hit0 = new Setting<String>("mania-hit0");
		protected final Setting<String> hit50 = new Setting<String>("mania-hit50");
		protected final Setting<String> hit100 = new Setting<String>("mania-hit100");
		protected final Setting<String> hit200 = new Setting<String>("mania-hit200");
		protected final Setting<String> hit300 = new Setting<String>("mania-hit300");
		protected final Setting<String> hit300g = new Setting<String>("mania-hit300g");

		private ManiaIni(int keys){
			this.keys = keys;
			columnSpacing.update(fillArray(keys - 1, 0.0D));
			columnWidth.update(fillArray(keys, 30.0D));
			columnLineWidth.update(fillArray(keys + 1, 2.0D));
			lightingNWidth.update(fillArray(keys, 0.0D));
			lightingLWidth.update(fillArray(keys, 0.0D));
			columns = new Column[keys];
			for(int i = 0; i < keys; i++){
				columns[i] = new Column();
			}
		}

		private static final double[] fillArray(int len, double value){
			double[] array = new double[len];
			for(int i = 0; i < len; i++){
				array[i] = value;
			}
			return array;
		}

		protected static final class Column{
			protected int key;

			protected Boolean keyFlipWhenUpsideDown = null;
			protected Boolean keyFlipWhenUpsideDownD = null;
			protected Boolean noteFlipWhenUpsideDown = null;
			protected Boolean noteFlipWhenUpsideDownH = null;
			protected Boolean noteFlipWhenUpsideDownL = null;
			protected Boolean noteFlipWhenUpsideDownT = null;

			protected int noteBodyStyle = -1;//0, 1, 2, -1=undefined

			protected Colour colour = new Colour(0, 0, 0, 255);
			protected Colour colourLight = new Colour(255, 255, 255);

			protected String keyImage = null;
			protected String keyImageD = null;
			protected String noteImage = null;
			protected String noteImageH = null;
			protected String noteImageL = null;
			protected String noteImageT = null;
		}

		public void write(PrintWriter writer){
			writer.println("[Mania]");
			writer.println("Keys: " + keys);
			writer.println("ColumnStart: " + columnStart);
			writer.println("ColumnRight: " + columnRight);
			if(keys != 1){
				writer.println("ColumnSpacing: " + arrayToList(columnSpacing));
			}
			writer.println("ColumnWidth: " + arrayToList(columnWidth));
			writer.println("ColumnLineWidth: " + arrayToList(columnLineWidth));
			writer.println("BarlineHeight: " + barlineHeight);
			if(lightingNWidth != null){
				writer.println("LightingNWidth: " + arrayToList(lightingNWidth));
			}
			if(lightingLWidth != null){
				writer.println("LightingLWidth: " + arrayToList(lightingLWidth));
			}
			if(widthForNoteHeightScale != -1.0D){
				writer.println("WidthForNoteHeightScale: " + widthForNoteHeightScale);
			}
			writer.println("HitPosition: " + hitPosition);
			writer.println("LightPosition: " + lightPosition);
			writer.println("ScorePosition: " + scorePosition);
			writer.println("ComboPosition: " + comboPosition);
			writer.println("JudgementLine: " + (judgementLine ? 1 : 0));
			if(keys % 2 == 0 && keys >= 4){
				writer.println("SpecialStyle: " + specialStyle);
			}
			writer.println("ComboBurstStyle: " + comboBurstStyle);
			if(splitStages != null){
				writer.println("SplitStages: " + (splitStages ? 1 : 0));
			}
			writer.println("StageSeparation: " + stageSeparation);
			writer.println("SeparateScore: " + (separateScore ? 1 : 0));
			writer.println("KeysUnderNotes: " + (keysUnderNotes ? 1 : 0));
			writer.println("UpsideDown: " + (upsideDown ? 1 : 0));
			writer.println("KeyFlipWhenUpsideDown: " + (keyFlipWhenUpsideDown ? 1 : 0));
			writer.println("NoteFlipWhenUpsideDown: " + (noteFlipWhenUpsideDown ? 1 : 0));
			writer.println("NoteBodyStyle: " + noteBodyStyle);
			writer.println("ColourColumnLine: " + rgba(colourColumnLine));
			writer.println("ColourBarline: " + rgba(colourBarline));
			writer.println("ColourJudgementLine: " + rgb(colourJudgementLine));
			writer.println("ColourKeyWarning: " + rgb(colourKeyWarning));
			writer.println("ColourHold: " + rgba(colourHold));
			writer.println("ColourBreak: " + rgb(colourBreak));
			writer.println("StageLeft: " + stageLeft);
			writer.println("StageRight: " + stageRight);
			writer.println("StageBottom: " + stageBottom);
			writer.println("StageHint: " + stageHint);
			writer.println("StageLight: " + stageLight);
			writer.println("LightingN: " + lightingN);
			writer.println("LightingL: " + lightingL);
			writer.println("WarningArrow: " + warningArrow);
			writer.println("Hit0: " + hit0);
			writer.println("Hit50: " + hit50);
			writer.println("Hit100: " + hit100);
			writer.println("Hit200: " + hit200);
			writer.println("Hit300: " + hit300);
			writer.println("Hit300g: " + hit300g);
			
			for(int i = 0; i < columns.length; i++){
				Column col = columns[i];

				if(col.keyFlipWhenUpsideDown != null){
					writer.println("KeyFlipWhenUpsideDown" + col.key + ": " + (col.keyFlipWhenUpsideDown ? 1 : 0));
				}
				if(col.keyFlipWhenUpsideDownD != null){
					writer.println("KeyFlipWhenUpsideDown" + col.key + "D: " + (col.keyFlipWhenUpsideDownD ? 1 : 0));
				}
				if(col.noteFlipWhenUpsideDown != null){
					writer.println("NoteFlipWhenUpsideDown" + col.key + ": " + (col.noteFlipWhenUpsideDown ? 1 : 0));
				}
				if(col.noteFlipWhenUpsideDownH != null){
					writer.println("NoteFlipWhenUpsideDown" + col.key + "H: " + (col.noteFlipWhenUpsideDownH ? 1 : 0));
				}
				if(col.noteFlipWhenUpsideDownL != null){
					writer.println("NoteFlipWhenUpsideDown" + col.key + "L: " + (col.noteFlipWhenUpsideDownL ? 1 : 0));
				}
				if(col.noteFlipWhenUpsideDownT != null){
					writer.println("NoteFlipWhenUpsideDown" + col.key + "T: " + (col.noteFlipWhenUpsideDownT ? 1 : 0));
				}
				if(col.noteBodyStyle != -1){
					writer.println("NoteBodyStyle" + col.key + ": " + col.noteBodyStyle);
				}
				if(col.colour != null){
					writer.println("Colour" + col.key + ": " + rgba(col.colour));
				}
				if(col.colourLight != null){
					writer.println("ColourLight" + col.key + ": " + rgb(col.colourLight));
				}
				if(col.keyImage != null){
					writer.println("KeyImage" + col.key + ": " + col.keyImage);
				}
				if(col.keyImageD != null){
					writer.println("KeyImage" + col.key + "D: " + col.keyImageD);
				}
				if(col.noteImage != null){
					writer.println("NoteImage" + col.key + ": " + col.noteImage);
				}
				if(col.noteImageH != null){
					writer.println("NoteImage" + col.key + "H: " + col.noteImageH);
				}
				if(col.noteImageL != null){
					writer.println("NoteImage" + col.key + "L: " + col.noteImageL);
				}
				if(col.noteImageT != null){
					writer.println("NoteImage" + col.key + "T: " + col.noteImageT);
				}
			}
			
			writer.println();
		}
	}
	
	public void readIni(File file) throws IOException{
		List<Section> data = new ArrayList<Section>();
		Section section = new Section(null);
		data.add(section);
		Pattern header = Pattern.compile("[.*]");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while((line = reader.readLine()) != null){
			if(header.matcher(line.trim()).matches()){
				section = new Section(line.trim());
				data.add(section);
			}else{
				
			}
		}
		
		reader.close();
	}

	public void parse(File file) throws IOException{
		String line = null;
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((line = reader.readLine()) != null){
				if(line.trim().isEmpty() || line.startsWith("//")){
					continue;
				}
				String[] args = line.split(":", 2);
				if(!line.startsWith("[")){
					args[1] = args[1].trim();
				}
				switch(args[0]){
				//[Mania]
				case "[Mania]":
					readMania(reader);
					break;
				//[General]
				case "Name":
					name = args[1];
					break;
				case "Author":
					author = args[1];
					break;
				case "Version":
					version = Version.fromString(args[1]);
					if(version == null){
						usedDefault = true;
						version = Version.V25;
					}
					break;
				case "CursorExpand":
					if(args[1].equals("1") || args[1].equals("0")){
						cursorExpand = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "CursorCentre":
					if(args[1].equals("1") || args[1].equals("0")){
						cursorCentre = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "CursorRotate":
					if(args[1].equals("1") || args[1].equals("0")){
						cursorRotate = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "CursorTrailRotate":
					if(args[1].equals("1") || args[1].equals("0")){
						cursorTrailRotate = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "AnimationFramerate":
					try{
						int val = Integer.parseInt(args[1]);
						if(val > 0){
							animationFramerate = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "LayeredHitSounds":
					if(args[1].equals("1") || args[1].equals("0")){
						layeredHitSounds = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "ComboBurstRandom":
					if(args[1].equals("1") || args[1].equals("0")){
						comboBurstRandom = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "CustomComboBurstSounds":
					customComboBurstSounds = args[1].replaceAll(" ", "");
					break;
				case "HitCircleOverlayAboveNumber":
					if(args[1].equals("1") || args[1].equals("0")){
						hitCircleOverlayAboveNumber = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "SliderStyle":
					try{
						int style = Integer.parseInt(args[1]);
						if(style >= 0 && style <= 2){
							sliderStyle = style;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "SliderBallFlip":
					if(args[1].equals("1") || args[1].equals("0")){
						sliderBallFlip = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "AllowSliderBallTint":
					if(args[1].equals("1") || args[1].equals("0")){
						allowSliderBallTint = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "SpinnerNoBlink":
					if(args[1].equals("1") || args[1].equals("0")){
						spinnerNoBlink = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "SpinnerFadePlayfield":
					if(args[1].equals("1") || args[1].equals("0")){
						spinnerFadePlayfield = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "SpinnerFrequencyModulate":
					if(args[1].equals("1") || args[1].equals("0")){
						spinnerFrequencyModulate = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				//[Colours]
				case "SongSelectActiveText":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							songSelectActiveText = color;
						}
					}
					break;
				case "SongSelectInactiveText":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							songSelectInactiveText = color;
						}
					}
					break;
				case "MenuGlow":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							menuGlow = color;
						}
					}
					break;
				case "StarBreakAdditive":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							starBreakAdditive = color;
						}
					}
					break;
				case "InputOverlayText":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							inputOverlayText = color;
						}
					}
					break;
				case "SliderBall":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							sliderBall = color;
						}
					}
					break;
				case "SliderTrackOverride":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							sliderTrackOverride = color;
						}
					}
					break;
				case "SliderBorder":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							sliderBorder = color;
						}
					}
					break;
				case "SpinnerBackground":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							spinnerBackground = color;
						}
					}
					break;
				case "Combo1":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo1 = color;
						}
					}
					break;
				case "Combo2":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo2 = color;
						}
					}
					break;
				case "Combo3":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo3 = color;
						}
					}
					break;
				case "Combo4":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo4 = color;
						}
					}
					break;
				case "Combo5":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo5 = color;
						}
					}
					break;
				case "Combo6":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo6 = color;
						}
					}
					break;
				case "Combo7":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo7 = color;
						}
					}
					break;
				case "Combo8":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							combo8 = color;
						}
					}
					break;
				//[Fonts]
				case "HitCirclePrefix":
					hitCirclePrefix = args[1];
					break;
				case "HitCircleOverlap":
					hitCircleOverlap = Integer.parseInt(args[1]);
					break;
				case "ScorePrefix":
					scorePrefix = args[1];
					break;
				case "ScoreOverlap":
					scoreOverlap = Integer.parseInt(args[1]);
					break;
				case "ComboPrefix":
					comboPrefix = args[1];
					break;
				case "ComboOverlap":
					comboOverlap = Integer.parseInt(args[1]);
					break;
				//[CatchTheBeat]
				case "HyperDash":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							hyperDash = color;
						}
					}
					break;
				case "HyperDashFruit":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							hyperDashFruit = color;
						}
					}
					break;
				case "HyperDashAfterImage":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							hyperDashAfterImage = color;
						}
					}
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("Line: " + line, e);
		}
		if(usedDefault){
			JOptionPane.showMessageDialog(SkinChecker.frame, "Skin.ini fields were found that couldn't be parsed. Default values were used.", "Skin Checker", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void readMania(BufferedReader reader) throws IOException{
		String line;
		do{
			line = reader.readLine();
		}while(line.trim().isEmpty() || line.startsWith("//"));
		int keys = Integer.parseInt(line.trim().substring(5).trim());
		if(keys <= 0 || keys > 10){
			throw new IllegalArgumentException("Unsupported mania key count: " + keys);
		}
		
		ManiaIni ini = new ManiaIni(keys);
		mania[keys - 1] = ini;
		
		try{
			reader.mark(1);
			int start;
			while((start = reader.read()) != -1){
				if(start == '['){
					reader.reset();
					return;
				}else{
					line = (char)start + reader.readLine();
				}
				reader.mark(1);
				if(line.trim().isEmpty() || line.startsWith("//")){
					continue;
				}
				String[] args = line.split(":");
				args[1] = args[1].trim();
				switch(args[0]){
				case "ColumnStart":
					try{
						double val = Double.parseDouble(args[1]);
						if(val >= 0.0D){
							ini.columnStart = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "ColumnRight":
					try{
						double val = Double.parseDouble(args[1]);
						if(val >= 0.0D){
							ini.columnRight = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "ColumnSpacing":
					try{
						ini.columnSpacing = parseList(args[1], keys - 1);
					}catch(IllegalArgumentException e){
						usedDefault = true;
					}
					break;
				case "ColumnWidth":
					try{
						ini.columnWidth = parseList(args[1], keys);
					}catch(IllegalArgumentException e){
						usedDefault = true;
					}
					break;
				case "ColumnLineWidth":
					try{
						ini.columnLineWidth = parseList(args[1], keys + 1);
					}catch(IllegalArgumentException e){
						usedDefault = true;
					}
					break;
				case "BarlineHeight":
					try{
						double val = Double.parseDouble(args[1]);
						if(val >= 0.0D){
							ini.barlineHeight = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "LightingNWidth":
					try{
						ini.lightingNWidth = parseList(args[1], keys);
					}catch(IllegalArgumentException e){
						usedDefault = true;
					}
					break;
				case "LightingLWidth":
					try{
						ini.lightingLWidth = parseList(args[1], keys);
					}catch(IllegalArgumentException e){
						usedDefault = true;
					}
					break;
				case "WidthForNoteHeightScale":
					try{
						double val = Double.parseDouble(args[1]);
						if(val >= 0.0D){
							ini.widthForNoteHeightScale = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "HitPosition":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0){
							ini.hitPosition = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "LightPosition":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0){
							ini.lightPosition = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "ScorePosition":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0){
							ini.scorePosition = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "ComboPosition":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0){
							ini.comboPosition = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "JudgementLine":
					if(args[1].equals("1") || args[1].equals("0")){
						ini.judgementLine = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "SpecialStyle":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0 && val <= 2){
							ini.specialStyle = val;
						}
					}catch(NumberFormatException e){
						switch(args[1].toLowerCase(Locale.ROOT)){
						case "none":
							ini.specialStyle = 0;
							break;
						case "left":
							ini.specialStyle = 1;
							break;
						case "right":
							ini.specialStyle = 2;
							break;
						}
					}
					break;
				case "ComboBurstStyle":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0 && val <= 2){
							ini.comboBurstStyle = val;
						}
					}catch(NumberFormatException e){
						switch(args[1].toLowerCase(Locale.ROOT)){
						case "left":
							ini.comboBurstStyle = 0;
							break;
						case "right":
							ini.comboBurstStyle = 1;
							break;
						case "both":
							ini.comboBurstStyle = 2;
							break;
						}
					}
					break;
				case "SplitStages":
					if(args[1].equals("1") || args[1].equals("0")){
						ini.splitStages = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "StageSeparation":
					try{
						int val = Integer.parseInt(args[1]);
						if(val >= 0){
							ini.stageSeparation = val;
						}else{
							usedDefault = true;
						}
					}catch(NumberFormatException e){
						usedDefault = true;
					}
					break;
				case "SeparateScore":
					if(args[1].equals("1") || args[1].equals("0")){
						ini.separateScore = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "KeysUnderNotes":
					if(args[1].equals("1") || args[1].equals("0")){
						ini.keysUnderNotes = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "UpsideDown":
					if(args[1].equals("1") || args[1].equals("0")){
						ini.upsideDown = args[1].equals("1");
					}else{
						usedDefault = true;
					}
					break;
				case "ColourColumnLine":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourColumnLine = color;
						}
					}
					break;
				case "ColourBarline":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourBarline = color;
						}
					}
					break;
				case "ColourJudgementLine":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourJudgementLine = color;
						}
					}
					break;
				case "ColourKeyWarning":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourKeyWarning = color;
						}
					}
					break;
				case "ColourHold":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourHold = color;
						}
					}
					break;
				case "ColourBreak":
					{
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.colourBreak = color;
						}
					}
					break;
				case "StageLeft":
					ini.stageLeft = args[1];
					break;
				case "StageRight":
					ini.stageRight = args[1];
					break;
				case "StageBottom":
					ini.stageBottom = args[1];
					break;
				case "StageHint":
					ini.stageHint = args[1];
					break;
				case "StageLight":
					ini.stageLight = args[1];
					break;
				case "LightingN":
					ini.lightingN = args[1];
					break;
				case "LightingL":
					ini.lightingL = args[1];
					break;
				case "WarningArrow":
					ini.warningArrow = args[1];
					break;
				case "Hit0":
					ini.hit0 = args[1];
					break;
				case "Hit50":
					ini.hit50 = args[1];
					break;
				case "Hit100":
					ini.hit100 = args[1];
					break;
				case "Hit200":
					ini.hit200 = args[1];
					break;
				case "Hit300":
					ini.hit300 = args[1];
					break;
				case "Hit300g":
					ini.hit300g = args[1];
					break;
				default:
					if(args[0].startsWith("KeyFlipWhenUpsideDown")){
						args[0] = args[0].substring(21);
						if(args[0].isEmpty()){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.keyFlipWhenUpsideDown = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else if(args[0].endsWith("D")){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].keyFlipWhenUpsideDownD = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else{
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0])].keyFlipWhenUpsideDown = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}
					}else if(args[0].startsWith("NoteFlipWhenUpsideDown")){
						args[0] = args[0].substring(22);
						if(args[0].isEmpty()){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.noteFlipWhenUpsideDown = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else if(args[0].endsWith("H")){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteFlipWhenUpsideDownH = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else if(args[0].endsWith("L")){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteFlipWhenUpsideDownL = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else if(args[0].endsWith("T")){
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteFlipWhenUpsideDownT = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}else{
							if(args[1].equals("1") || args[1].equals("0")){
								ini.columns[Integer.parseInt(args[0])].noteFlipWhenUpsideDown = args[1].equals("1");
							}else{
								usedDefault = true;
							}
						}
					}else if(args[0].startsWith("NoteBodyStyle")){
						args[0] = args[0].substring(13);
						if(args[0].isEmpty()){
							try{
								int val = Integer.parseInt(args[1]);
								if(val >= 0 && val <= 2){
									ini.noteBodyStyle = val;
								}else{
									usedDefault = true;
								}
							}catch(NumberFormatException e){
								usedDefault = true;
							}
						}else{
							try{
								int val = Integer.parseInt(args[1]);
								if(val >= 0 && val <= 2){
									ini.columns[Integer.parseInt(args[0])].noteBodyStyle = val;
								}else{
									usedDefault = true;
								}
							}catch(NumberFormatException e){
								usedDefault = true;
							}
						}
					}else if(args[0].startsWith("ColourLight")){
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.columns[Integer.parseInt(args[0].substring(11)) - 1].colourLight = color;
						}
					}else if(args[0].startsWith("Colour")){
						Colour color = parseColor(args[1]);
						if(color != null){
							ini.columns[Integer.parseInt(args[0].substring(6)) - 1].colour = color;
						}
					}else if(args[0].startsWith("KeyImage")){
						args[0] = args[0].substring(8);
						if(args[0].endsWith("D")){
							ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].keyImage = args[1];
						}else{
							ini.columns[Integer.parseInt(args[0])].keyImage = args[1];
						}
					}else if(args[0].startsWith("NoteImage")){
						args[0] = args[0].substring(9);
						if(args[0].endsWith("H")){
							ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteImageH = args[1];
						}else if(args[0].endsWith("T")){
							ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteImageT = args[1];
						}else if(args[0].endsWith("L")){
							ini.columns[Integer.parseInt(args[0].substring(0, args[0].length() - 1))].noteImageL = args[1];
						}else{
							ini.columns[Integer.parseInt(args[0])].noteImage = args[1];
						}
					}
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("Line: " + line, e);
		}
	}
	
	private double[] parseList(String data, int expected){
		String[] args = data.split(",");
		if(args.length != expected){
			throw new IllegalArgumentException("Illegal number of arguments on line: " + data);
		}
		double[] values = new double[args.length];
		for(int i = 0; i < values.length; i++){
			values[i] = Math.max(0.0D, Double.parseDouble(args[i].trim()));
		}
		return values;
	}

	private Colour parseColor(String arg){
		String[] args = arg.split(",");
		try{
			if(args.length == 3){
				return new Colour(Integer.parseInt(args[0].trim()), Integer.parseInt(args[1].trim()), Integer.parseInt(args[2].trim()));
			}else if(args.length == 4){
				return new Colour(Integer.parseInt(args[0].trim()), Integer.parseInt(args[1].trim()), Integer.parseInt(args[2].trim()), Integer.parseInt(args[3].trim()));
			}else{
				usedDefault = true;
				return null;
			}
		}catch(NumberFormatException e){
			usedDefault = true;
			return null;
		}
	}

	public void writeIni(File file) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(new FileOutputStream(file));

		writer.println("[General]");
		writer.println("Name: " + name);
		writer.println("Author: " + author);
		writer.println("Version: " + version.name);
		writer.println();
		writer.println("CursorExpand: " + (cursorExpand ? 1 : 0));
		writer.println("CursorCentre: " + (cursorCentre ? 1 : 0));
		writer.println("CursorRotate: " + (cursorRotate ? 1 : 0));
		writer.println("CursorTrailRotate: " + (cursorTrailRotate ? 1 : 0));
		if(animationFramerate != -1){
			writer.println("AnimationFramerate: " + animationFramerate);
		}
		writer.println("LayeredHitSounds: " + (layeredHitSounds ? 1 : 0));
		writer.println("ComboBurstRandom: " + (comboBurstRandom ? 1 : 0));
		if(customComboBurstSounds != null){
			writer.println("CustomComboBurstSounds: " + customComboBurstSounds);
		}
		writer.println("HitCircleOverlayAboveNumber: " + (hitCircleOverlayAboveNumber ? 1 : 0));
		writer.println("SliderStyle: " + sliderStyle);
		writer.println("SliderBallFlip: " + (sliderBallFlip ? 1 : 0));
		writer.println("AllowSliderBallTint: " + (allowSliderBallTint ? 1 : 0));
		writer.println("SpinnerNoBlink: " + (spinnerNoBlink ? 1 : 0));
		writer.println("SpinnerFadePlayfield: " + (spinnerFadePlayfield ? 1 : 0));
		writer.println("SpinnerFrequencyModulate: " + (spinnerFrequencyModulate ? 1 : 0));
		writer.println();

		writer.println("[Colours]");
		writer.println("SongSelectActiveText: " + rgb(songSelectActiveText));
		writer.println("SongSelectInactiveText: " + rgb(songSelectInactiveText));
		writer.println("MenuGlow: " + rgb(menuGlow));
		writer.println("StarBreakAdditive: " + rgb(starBreakAdditive));
		writer.println("InputOverlayText: " + rgb(inputOverlayText));
		writer.println("SliderBall: " + rgb(sliderBall));
		if(sliderTrackOverride != null){
			writer.println("SliderTrackOverride: " + rgb(sliderTrackOverride));
		}
		writer.println("SliderBorder: " + rgb(sliderBorder));
		writer.println("SpinnerBackground: " + rgb(spinnerBackground));
		writer.println("Combo1: " + rgb(combo1));
		if(combo2 != null){
			writer.println("Combo2: " + rgb(combo2));
		}
		if(combo3 != null){
			writer.println("Combo3: " + rgb(combo3));
		}
		if(combo4 != null){
			writer.println("Combo4: " + rgb(combo4));
		}
		if(combo5 != null){
			writer.println("Combo5: " + rgb(combo5));
		}
		if(combo6 != null){
			writer.println("Combo6: " + rgb(combo6));
		}
		if(combo7 != null){
			writer.println("Combo7: " + rgb(combo7));
		}
		if(combo8 != null){
			writer.println("Combo8: " + rgb(combo8));
		}
		writer.println();

		writer.println("[Fonts]");
		writer.println("HitCirclePrefix: " + hitCirclePrefix);
		writer.println("HitCircleOverlap: " + hitCircleOverlap);
		writer.println("ScorePrefix: " + scorePrefix);
		writer.println("ScoreOverlap: " + scoreOverlap);
		writer.println("ComboPrefix: " + comboPrefix);
		writer.println("ComboOverlap: " + comboOverlap);
		writer.println();

		writer.println("[CatchTheBeat]");
		writer.println("HyperDash: " + rgb(hyperDash));
		if(hyperDashFruit != null){
			writer.println("HyperDashFruit: " + rgb(hyperDashFruit));
		}
		if(hyperDashAfterImage != null){
			writer.println("HyperDashAfterImage: " + rgb(hyperDashAfterImage));
		}
		writer.println();

		for(ManiaIni ini : mania){
			if(ini != null){
				ini.write(writer);
			}
		}

		writer.flush();
		writer.close();
	}
	
	private final void write(PrintWriter writer, String key, String value){
		
	}
	
	private final void writeComments(String key){
		
	}

	private static final String rgb(Colour color){
		return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
	}

	private static final String rgba(Colour color){
		if(!color.hasAlpha()){
			return rgb(color);
		}else{
			return rgb(color) + "," + color.getAlpha();
		}
	}

	private static final String arrayToList(double[] array){
		StringJoiner joiner = new StringJoiner(",");
		for(double d : array){
			joiner.add(String.valueOf(d));
		}
		return joiner.toString();
	}
	
	protected enum SliderStyle{
		SEGMENTS("Segments"),
		GRADIENT("Gradient");
		
		private final String name;
		
		private SliderStyle(String name){
			this.name = name;
		}
	
		@Override
		public String toString(){
			return name;
		}
	}
	
	protected enum SpecialStyle{
		
	}
	
	protected enum ComboBurstStyle{
		
	}
	
	protected enum NoteBodyStyle{
		
	}

	protected enum Version{
		V1("1", "(Old style)"),
		V2("2", "(Basic new style)"),
		V21("2.1", "(Taiko position changes)"),
		V22("2.2", "(UI changes)"),
		V23("2.3", "(New Catch catcher style)"),
		V24("2.4", "(Mania stage scaling reduction)"),
		V25("2.5", "(Mania upscroll and column improvements)"),
		LATEST("latest", "(for personal skins)");

		public final String name;
		public final String extra;

		private Version(String name, String extra){
			this.name = name;
			this.extra = extra;
		}

		private static Version fromString(String str){
			switch(str){
			case "1":
				return V1;
			case "2":
			case "2.0":
				return V2;
			case "2.1":
				return V21;
			case "2.2":
				return V22;
			case "2.3":
				return V23;
			case "2.4":
				return V24;
			case "2.5":
				return V25;
			case "latest":
				return LATEST;
			default:
				return null;
			}
		}

		@Override
		public String toString(){
			return name + " " + extra;
		}
	}
}
