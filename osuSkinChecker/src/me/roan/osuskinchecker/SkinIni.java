package me.roan.osuskinchecker;

import java.awt.Color;
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
import me.roan.osuskinchecker.SkinIni.SpecialStyle;

public class SkinIni{
	private static boolean usedDefault = false;
	private List<Section> data;
	
	//general
	protected final Setting<String> name = new Setting<String>("Name", "-");
	protected final Setting<String> author = new Setting<String>("Author", "-");
	protected final Setting<Version> version = new Setting<Version>("Version", Version.V1);
	protected final Setting<Boolean> cursorExpand = new Setting<Boolean>("CursorExpand", true);
	protected final Setting<Boolean> cursorCentre = new Setting<Boolean>("CursorCentre", true);
	protected final Setting<Boolean> cursorRotate = new Setting<Boolean>("CursorRotate", true);
	protected final Setting<Boolean> cursorTrailRotate = new Setting<Boolean>("CursorTrailRotate", true);
	protected final Setting<Integer> animationFramerate = new Setting<Integer>("AnimationFramerate", false, 1);//non negative

	//combo bursts
	protected final Setting<Boolean> layeredHitSounds = new Setting<Boolean>("LayeredHitSounds", true);
	protected final Setting<Boolean> comboBurstRandom = new Setting<Boolean>("ComboBurstRandom", false);
	protected final Setting<String> customComboBurstSounds = new Setting<String>("CustomComboBurstSounds", false, "");//list of ints, positive values only

	//standard
	protected final Setting<Boolean> hitCircleOverlayAboveNumber = new Setting<Boolean>("HitCircleOverlayAboveNumber", true);
	protected final Setting<SliderStyle> sliderStyle = new Setting<SliderStyle>("SliderStyle", SliderStyle.GRADIENT);//1 or 2
	protected final Setting<Boolean> sliderBallFlip = new Setting<Boolean>("SliderBallFlip", false);
	protected final Setting<Boolean> allowSliderBallTint = new Setting<Boolean>("AllowSliderBallTint", false);
	protected final Setting<Boolean> spinnerNoBlink = new Setting<Boolean>("SpinnerNoBlink", false);
	protected final Setting<Boolean> spinnerFadePlayfield = new Setting<Boolean>("SpinnerFadePlayfield", false);
	protected final Setting<Boolean> spinnerFrequencyModulate = new Setting<Boolean>("SpinnerFrequencyModulate", true);

	//colours
	protected final Setting<Colour> songSelectActiveText = new Setting<Colour>("SongSelectActiveText", new Colour(0, 0, 0));
	protected final Setting<Colour> songSelectInactiveText = new Setting<Colour>("SongSelectInactiveText", new Colour(255, 255, 255));
	protected final Setting<Colour> menuGlow = new Setting<Colour>("MenuGlow", new Colour(0, 78, 155));
	
	protected final Setting<Colour> starBreakAdditive = new Setting<Colour>("StarBreakAdditive", new Colour(255, 182, 193));
	protected final Setting<Colour> inputOverlayText = new Setting<Colour>("InputOverlayText", new Colour(0, 0, 0));
	
	protected final Setting<Colour> sliderBall = new Setting<Colour>("SliderBall", new Colour(2, 170, 255));
	protected final Setting<Colour> sliderTrackOverride = new Setting<Colour>("SliderTrackOverride", new Colour(0, 0, 0));
	protected final Setting<Colour> sliderBorder = new Setting<Colour>("SliderBorder", new Colour(0, 0, 0));
	protected final Setting<Colour> spinnerBackground = new Setting<Colour>("SpinnerBackground", new Colour(100, 100, 100));
	
	protected final Setting<Colour> combo1 = new Setting<Colour>("Combo1", new Colour(255, 192, 0));
	protected final Setting<Colour> combo2 = new Setting<Colour>("Combo2", new Colour(0, 0, 0));
	protected final Setting<Colour> combo3 = new Setting<Colour>("Combo3", new Colour(0, 0, 0));
	protected final Setting<Colour> combo4 = new Setting<Colour>("Combo4", new Colour(0, 0, 0));
	protected final Setting<Colour> combo5 = new Setting<Colour>("Combo5", new Colour(0, 0, 0));
	protected final Setting<Colour> combo6 = new Setting<Colour>("Combo6", new Colour(0, 0, 0));
	protected final Setting<Colour> combo7 = new Setting<Colour>("Combo7", new Colour(0, 0, 0));
	protected final Setting<Colour> combo8 = new Setting<Colour>("Combo8", new Colour(0, 0, 0));

	//fonts
	protected final Setting<String> hitCirclePrefix = new Setting<String>("HitCirclePrefix", "default");
	protected final Setting<Integer> hitCircleOverlap = new Setting<Integer>("HitCircleOverlap", -2);//negative allowed

	protected final Setting<String> scorePrefix = new Setting<String>("ScorePrefix", "score");
	protected final Setting<Integer> scoreOverlap = new Setting<Integer>("ScoreOverlap", -2);//negative allowed

	protected final Setting<String> comboPrefix = new Setting<String>("ComboPrefix", "score");
	protected final Setting<Integer> comboOverlap = new Setting<Integer>("ComboOverlap", -2);//negative allowed

	//ctb
	protected final Setting<Colour> hyperDash = new Setting<Colour>("HyperDash", new Colour(255, 0, 0));
	protected final Setting<Colour> hyperDashFruit = new Setting<Colour>("HyperDashFruit", new Colour(0, 0, 0));
	protected final Setting<Colour> hyperDashAfterImage = new Setting<Colour>("HyperDashAfterImage", new Colour(0, 0, 0));

	protected final ManiaIni[] mania = new ManiaIni[ManiaIni.MAX_KEYS];

	public final void createManiaConfiguration(int keys){
		mania[keys - 1] = new ManiaIni(keys);
	}

	protected static final class ManiaIni{
		protected static final int MAX_KEYS = 10;
		protected int keys;//non negative
		protected final Setting<Double> columnStart = new Setting<Double>("ColumnStart", 136.0D);
		protected final Setting<Double> columnRight = new Setting<Double>("ColumnRight", 19.0D);
		protected final Setting<double[]> columnSpacing = new Setting<double[]>("ColumnSpacing", null);//n-1 numbers
		protected final Setting<double[]> columnWidth = new Setting<double[]>("ColumnWidth", null);//n numbers
		protected final Setting<double[]> columnLineWidth = new Setting<double[]>("ColumnLineWidth", null);//n+1 numbers
		protected final Setting<Double> barlineHeight = new Setting<Double>("BarlineHeight", 1.2D);
		protected final Setting<double[]> lightingNWidth = new Setting<double[]>("LightingNWidth", null);//n numbers
		protected final Setting<double[]> lightingLWidth = new Setting<double[]>("LightingLWidth", null);//n numbers
		protected final Setting<Double> widthForNoteHeightScale = new Setting<Double>("WidthForNoteHeightScale", -1.0D);
		protected final Setting<Integer> hitPosition = new Setting<Integer>("HitPosition", 402);
		protected final Setting<Integer> lightPosition = new Setting<Integer>("LightPosition", 413);
		protected final Setting<Integer> scorePosition = new Setting<Integer>("ScorePosition", 325);
		protected final Setting<Integer> comboPosition = new Setting<Integer>("ComboPosition", 111);
		protected final Setting<Boolean> judgementLine = new Setting<Boolean>("JudgementLine", true);
		protected final Setting<SpecialStyle> specialStyle = new Setting<SpecialStyle>("SpecialStyle", SpecialStyle.NONE);//0, 1 or 2
		protected final Setting<ComboBurstStyle> comboBurstStyle = new Setting<ComboBurstStyle>("ComboBurstStyle", ComboBurstStyle.RIGHT);//0, 1 or 2
		protected final Setting<Boolean> splitStages = new Setting<Boolean>("SplitStages", false, true);
		protected final Setting<Double> stageSeparation = new Setting<Double>("StageSeparation", 40.0D);
		protected final Setting<Boolean> separateScore = new Setting<Boolean>("SeparationScore", true);
		protected final Setting<Boolean> keysUnderNotes = new Setting<Boolean>("KeysUnderNotes", false);
		protected final Setting<Boolean> upsideDown = new Setting<Boolean>("UpsideDown", false);
		protected final Setting<Boolean> keyFlipWhenUpsideDown = new Setting<Boolean>("KeyFlipWhenUpsideDown", true);
		protected final Setting<Boolean> noteFlipWhenUpsideDown = new Setting<Boolean>("NoteFlipWhenUpsideDown", true);
		protected final Setting<NoteBodyStyle> noteBodyStyle = 1;//0, 1, 2
		protected final Setting<Colour> colourColumnLine = new Setting<Colour>("ColourColumnLine", new Colour(255, 255, 255, 255));
		protected final Setting<Colour> colourBarline = new Setting<Colour>("ColourBarline", new Colour(255, 255, 255, 255));
		protected final Setting<Colour> colourJudgementLine = new Setting<Colour>("ColourJudgementLine", new Colour(255, 255, 255));
		protected final Setting<Colour> colourKeyWarning = new Setting<Colour>("ColourKeyWarning", new Colour(0, 0, 0));
		protected final Setting<Colour> colourHold = new Setting<Colour>("ColourHold", new Colour(255, 191, 51, 255));
		protected final Setting<Colour> colourBreak = new Setting<Colour>("ColourBreak", new Colour(255, 0, 0));

		protected Column[] columns;

		protected final Setting<String> stageLeft = new Setting<String>("StageLeft", "mania-stage-left");
		protected final Setting<String> stageRight = new Setting<String>("StageRight", "mania-stage-right");
		protected final Setting<String> stageBottom = new Setting<String>("StageBottom", "mania-stage-bottom");
		protected final Setting<String> stageHint = new Setting<String>("StageHint", "mania-stage-hint");
		protected final Setting<String> stageLight = new Setting<String>("StageLight", "mania-stage-light");
		protected final Setting<String> lightingN = new Setting<String>("LightingN", "LightingN");
		protected final Setting<String> lightingL = new Setting<String>("LightingL", "LightingL");
		protected final Setting<String> warningArrow = new Setting<String>("WarningArrow", "mania-warningarrow");

		protected final Setting<String> hit0 = new Setting<String>("Hit0", "mania-hit0");
		protected final Setting<String> hit50 = new Setting<String>("Hit50", "mania-hit50");
		protected final Setting<String> hit100 = new Setting<String>("Hit100", "mania-hit100");
		protected final Setting<String> hit200 = new Setting<String>("Hit200", "mania-hit200");
		protected final Setting<String> hit300 = new Setting<String>("Hit300", "mania-hit300");
		protected final Setting<String> hit300g = new Setting<String>("Hit300g", "mania-hit300g");

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

			protected Setting<Boolean> keyFlipWhenUpsideDown = new Setting<Boolean>(false, true);
			protected Setting<Boolean> keyFlipWhenUpsideDownD = new Setting<Boolean>(false, true);
			protected Setting<Boolean> noteFlipWhenUpsideDown = new Setting<Boolean>(false, true);
			protected Setting<Boolean> noteFlipWhenUpsideDownH = new Setting<Boolean>(false, true);
			protected Setting<Boolean> noteFlipWhenUpsideDownL = new Setting<Boolean>(false, true);
			protected Setting<Boolean> noteFlipWhenUpsideDownT = new Setting<Boolean>(false, true);

			protected Setting<Integer> noteBodyStyle = new Setting<Integer>(false, 0);//0, 1, 2

			protected Setting<Colour> colour = new Setting<Colour>(new Colour(0, 0, 0, 255));
			protected Setting<Colour> colourLight = new Setting<Colour>(new Colour(255, 255, 255));

			protected Setting<String> keyImage = new Setting<String>(false, "");
			protected Setting<String> keyImageD = new Setting<String>(false, "");
			protected Setting<String> noteImage = new Setting<String>(false, "");
			protected Setting<String> noteImageH = new Setting<String>(false, "");
			protected Setting<String> noteImageL = new Setting<String>(false, "");
			protected Setting<String> noteImageT = new Setting<String>(false, "");
		}
	}
	
	public void readIni(File file) throws IOException{
		usedDefault = false;
		data = new ArrayList<Section>();
		Section section = new Section(null);
		data.add(section);
		Pattern header = Pattern.compile("[.*]");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while((line = reader.readLine()) != null){
			if(header.matcher(line.trim()).matches()){
				section = new Section(line.trim());
				data.add(section);
			}else if(section.isMania()){
				section.data.add(parseMania(line));
			}else{
				section.data.add(parse(line));
			}
		}
		
		reader.close();
	}

	public Setting<?> parse(String line) throws IOException{
		if(line.trim().isEmpty() || line.startsWith("//")){
			return new Comment(line);
		}
		String[] args = line.split(":", 2);
		args[1] = args[1].trim();
		switch(args[0]){
		//[General]
		case "Name":
			return name.update(args[1]);
		case "Author":
			return author.update(args[1]);
		case "Version":
			return version.update(Version.fromString(args[1]));
		case "CursorExpand":
			return parseBoolean(cursorExpand, args[1]);
		case "CursorCentre":
			return parseBoolean(cursorCentre, args[1]);
		case "CursorRotate":
			return parseBoolean(cursorRotate, args[1]);
		case "CursorTrailRotate":
			return parseBoolean(cursorTrailRotate, args[1]);
		case "AnimationFramerate":
			return parseInt(animationFramerate, args[1], 1);
		case "LayeredHitSounds":
			return parseBoolean(layeredHitSounds, args[1]);
		case "ComboBurstRandom":
			return parseBoolean(comboBurstRandom, args[1]);
		case "CustomComboBurstSounds":
			return customComboBurstSounds.update(args[1].replaceAll(" ", ""));
		case "HitCircleOverlayAboveNumber":
			return parseBoolean(hitCircleOverlayAboveNumber, args[1]);
		case "SliderStyle":
			return sliderStyle.update(SliderStyle.fromString(args[1]));
		case "SliderBallFlip":
			return parseBoolean(sliderBallFlip, args[1]);
		case "AllowSliderBallTint":
			return parseBoolean(allowSliderBallTint, args[1]);
		case "SpinnerNoBlink":
			return parseBoolean(spinnerNoBlink, args[1]);
		case "SpinnerFadePlayfield":
			return parseBoolean(spinnerFadePlayfield, args[1]);
		case "SpinnerFrequencyModulate":
			return parseBoolean(spinnerFrequencyModulate, args[1]);
		//[Colours]
		case "SongSelectActiveText":
			return parseColor(songSelectActiveText, args[1]);
		case "SongSelectInactiveText":
			return parseColor(songSelectInactiveText, args[1]);
		case "MenuGlow":
			return parseColor(menuGlow, args[1]);
		case "StarBreakAdditive":
			return parseColor(starBreakAdditive, args[1]);
		case "InputOverlayText":
			return parseColor(inputOverlayText, args[1]);
		case "SliderBall":
			return parseColor(sliderBall, args[1]);
		case "SliderTrackOverride":
			return parseColor(sliderTrackOverride, args[1]);
		case "SliderBorder":
			return parseColor(sliderBorder, args[1]);
		case "SpinnerBackground":
			return parseColor(spinnerBackground, args[1]);
		case "Combo1":
			return parseColor(combo1, args[1]);
		case "Combo2":
			return parseColor(combo2, args[1]);
		case "Combo3":
			return parseColor(combo3, args[1]);
		case "Combo4":
			return parseColor(combo4, args[1]);
		case "Combo5":
			return parseColor(combo5, args[1]);
		case "Combo6":
			return parseColor(combo6, args[1]);
		case "Combo7":
			return parseColor(combo7, args[1]);
		case "Combo8":
			return parseColor(combo8, args[1]);
		//[Fonts]
		case "HitCirclePrefix":
			return hitCirclePrefix.update(args[1]);
		case "HitCircleOverlap":
			return parseInt(hitCircleOverlap, args[1]);
		case "ScorePrefix":
			return scorePrefix.update(args[1]);
		case "ScoreOverlap":
			return parseInt(scoreOverlap, args[1]);
		case "ComboPrefix":
			return comboPrefix.update(args[1]);
		case "ComboOverlap":
			return parseInt(comboOverlap, args[1]);
		//[CatchTheBeat]
		case "HyperDash":
			return parseColor(hyperDash, args[1]);
		case "HyperDashFruit":
			return parseColor(hyperDashFruit, args[1]);
		case "HyperDashAfterImage":
			return parseColor(hyperDashAfterImage, args[1]);
		default:
			return new Comment(line);
		}

		//TODO
//		try{
//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//			while((line = reader.readLine()) != null){
//				
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new IllegalArgumentException("Line: " + line, e);
//		}
//		if(usedDefault){
//			JOptionPane.showMessageDialog(SkinChecker.frame, "Skin.ini fields were found that couldn't be parsed. Default values were used.", "Skin Checker", JOptionPane.WARNING_MESSAGE);
//		}
	}

	private void parseMania(BufferedReader reader) throws IOException{
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
	
	private Setting<Integer> parseInt(Setting<Integer> setting, String line){
		return parseInt(setting, line, Integer.MIN_VALUE);
	}
	
	private Setting<Integer> parseInt(Setting<Integer> setting, String line, int min){
		return parseInt(setting, line, min, Integer.MAX_VALUE);
	}
	
	private Setting<Integer> parseInt(Setting<Integer> setting, String line, int min, int max){
		try{
			int val = Integer.parseInt(line);
			if(val >= min && val <= max){
				return setting.update(val);
			}else{
				usedDefault = true;
			}
		}catch(NumberFormatException e){
			usedDefault = true;
		}
		return setting;
	}
	
	private Setting<Boolean> parseBoolean(Setting<Boolean> setting, String line){
		if(line.equals("1") || line.equals("0")){
			return setting.update(line.equals("1"));
		}else{
			usedDefault = true;
			return setting;
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

	private Setting<Colour> parseColor(Setting<Colour> setting, String arg){
		String[] args = arg.split(",");
		try{
			if(args.length == 3){
				return setting.update(new Colour(Integer.parseInt(args[0].trim()), Integer.parseInt(args[1].trim()), Integer.parseInt(args[2].trim())));
			}else if(args.length == 4){
				return setting.update(new Colour(Integer.parseInt(args[0].trim()), Integer.parseInt(args[1].trim()), Integer.parseInt(args[2].trim()), Integer.parseInt(args[3].trim())));
			}else{
				usedDefault = true;
			}
		}catch(NumberFormatException e){
			usedDefault = true;
		}
		return setting;
	}

	public void writeIni(File file) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(new FileOutputStream(file));

		for(Section section : data){
			if(section.name != null){
				writer.println(section.name);
			}
			for(Setting setting : section.data){
				if(setting.isEnabled()){
					writer.println(setting);
				}
			}
		}

		writer.flush();
		writer.close();
	}

	private static final String arrayToList(double[] array){
		StringJoiner joiner = new StringJoiner(",");
		for(double d : array){
			joiner.add(String.valueOf(d));
		}
		return joiner.toString();
	}
	
	protected enum SliderStyle implements Printable{
		SEGMENTS(1, "Segments"),
		GRADIENT(2, "Gradient");
		
		private final String name;
		private final int id;
		
		private SliderStyle(int id, String name){
			this.name = name;
			this.id = id;
		}
		
		private static final SliderStyle fromString(String str){
			switch(str){
			case "1":
				return SEGMENTS;
			case "2":
				return GRADIENT;
			default:
				usedDefault = true;
				return GRADIENT;
			}
		}
	
		@Override
		public String toString(){
			return name;
		}

		@Override
		public String print(){
			return String.valueOf(id);
		}
	}
	
	protected enum SpecialStyle implements Printable{
		NONE(0, "None"),
		LEFT(1, "Left lane SP - outer lanes DP"),
		RIGHT(2, "Right lane SP - outer lanes DP");
		
		private final String name;
		private final int id;
		
		private SpecialStyle(int id, String name){
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String toString(){
			return name;
		}

		@Override
		public String print(){
			return String.valueOf(id);
		}
	}
	
	protected enum ComboBurstStyle implements Printable{
		LEFT(0, "Left"),
		RIGHT(1, "Right"),
		BOTH(2, "Both");
		
		private final String name;
		private final int id;
		
		private ComboBurstStyle(int id, String name){
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String toString(){
			return name;
		}

		@Override
		public String print(){
			return String.valueOf(id);
		}
	}
	
	protected enum NoteBodyStyle implements Printable{
		;

		private final String name;
		private final int id;
		
		private NoteBodyStyle(int id, String name){
			this.name = name;
			this.id = id;
		}
		
		@Override
		public String print(){
			return String.valueOf(id);
		}
	}

	protected enum Version implements Printable{
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
				usedDefault = true;
				return V1;
			}
		}

		@Override
		public String toString(){
			return name + " " + extra;
		}

		@Override
		public String print(){
			return name;
		}
	}
	
	protected static abstract interface Printable{
		
		public abstract String print();
	}
}
