package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.fxgui.CalculatorGUI_Intf;
import com.businessapp.fxgui.CalculatorGUI_Intf.Token;

import java.util.ArrayList;


/**
 * Implementation of CalculatorLogicIntf that only displays Tokens
 * received from the Calculator UI.
 *
 */
class CalculatorLogic implements CalculatorLogicIntf {
	private CalculatorGUI_Intf view;
	private StringBuffer dsb = new StringBuffer();
	private final double VAT_RATE = 19.0;

	ArrayList<Character> operator = new ArrayList<>();
	ArrayList<Double> operand = new ArrayList<>();

	Character sign;
	double op1;
	double op2;
	double erg;
	String ergebnis;
	String wert;

	CalculatorLogic() {
	}

	@Override
	public void inject( ControllerIntf dep ) {
		this.view = (CalculatorGUI_Intf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		nextToken( Token.K_C );		// reset calculator
	}

	@Override
	public void stop() {
	}


	/**
     * Process next token received from UI controller.
     * <p>
     * Tokens are transformed into output into UI properties:
     * 	- CalculatorIntf.DISPLAY for numbers and
     * 	- CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
	public void nextToken( Token tok ) {
		try {
			switch( tok ) {
			case K_0:	appendBuffer( "0" ); break;
			case K_1:	appendBuffer( "1" ); break;
			case K_2:	appendBuffer( "2" ); break;
			case K_3:	appendBuffer( "3" ); break;
			case K_4:	appendBuffer( "4" ); break;
			case K_5:	appendBuffer( "5" ); break;
			case K_6:	appendBuffer( "6" ); break;
			case K_7:	appendBuffer( "7" ); break;
			case K_8:	appendBuffer( "8" ); break;
			case K_9:	appendBuffer( "9" );
				break;

			case K_1000:appendBuffer( "000" );
				break;

			case K_DIV:
				throw new ArithmeticException( "ERR: div by zero" );
			case K_MUL:	appendBuffer( "*" ); break;
			case K_PLUS:appendBuffer( "+" ); break;
			case K_MIN:	appendBuffer( "-" ); break;
			case K_EQ:	 verarbeiten();
				break;

			case K_VAT:
				view.writeSideArea(
						wert = dsb.toString());
				double netto = Double.valueOf(wert) / 1.19;
				String nettoBetrag = String.format("%.2f", netto);
				view.writeSideArea(
						"Brutto:  " + dsb + "\n"
								+ VAT_RATE + "% MwSt: " + String.format("%.2f", (Double.valueOf(wert) - netto)) + " \n"
								+ "Netto:  " + nettoBetrag
				);
				break;

			case K_DOT:	appendBuffer( "." );
				break;

			case K_BACK:
				dsb.setLength( Math.max( 0, dsb.length() - 1 ) );
				break;

			case K_C:
				view.writeSideArea( "" );
			case K_CE:
				dsb.delete( 0,  dsb.length() );
				break;

			default:
			}
			String display = dsb.length()==0? "0" : dsb.toString();
			view.writeTextArea( display );

		} catch( ArithmeticException e ) {
			view.writeTextArea( e.getMessage() );
		}
	}

	/*
	 * Private method(s).
	 */
	private void appendBuffer( String d ) {
		if( dsb.length() <= CalculatorGUI_Intf.DISPLAY_MAXDIGITS ) {
			dsb.append( d );
		}
	}

	void calcAddSub(int zeiger) {
		//---rekursive logik
		if (operator.size() <= zeiger) {
			//    calcAddSub(0);
			ergebnis = String.format("%.2f", operand.get(0));
			dsb.delete(0, dsb.length());
			appendBuffer(ergebnis);
			System.out.println("OP " + operand.get(0));
			System.out.println("erg " + ergebnis);
			System.out.println("dsb " + dsb);
			return;
		}

		if (operator.get(zeiger) != null) {
			sign = operator.get(zeiger);
			op1 = operand.get(zeiger);
			op2 = operand.get(zeiger + 1);

			if (sign == '+' || sign == '-') {
				if (sign == '+') {
					op1 = op1 + op2;
					operand.set(zeiger, op1);
					operand.remove(zeiger + 1);
					operator.remove(zeiger);

					//----> Manuell alle zellen überschreiben...
					System.out.println("Zeiger:" + zeiger + " Operator: " + operator.toString() + "Sign: " + sign);
					calcAddSub(zeiger);
				}

				if (sign == '-') {
					op1 = op1 - op2;
					operand.set(zeiger, op1);
					operand.remove(zeiger + 1);
					operator.remove(zeiger);

					System.out.println("Zeiger:" + zeiger + " Operator: " + operator.toString() + "Sign: " + sign);
					calcAddSub(zeiger);
				}
			}
		}
	}

	void calcMultDiv(int zeiger) {
		//---rekursive logik
		//--------------TODO--------->>> Auf Länge des Arrays prüfen. (if(Array.zice()==0){...}) und ARRAY am ENDE der Berechnung LEEREN!

		if (operator.size() <= zeiger) {
			calcAddSub(0);
////            ergebnis = operand.get(0).toString();
			//        dsb.delete(0, dsb.length());
			//       appendBuffer(ergebnis);
			//         System.out.println(dsb);
			return;
		}

		if (operator.get(zeiger) != null) {
			sign = operator.get(zeiger);
			op1 = operand.get(zeiger);
			op2 = operand.get(zeiger + 1);

			if (sign == '+' || sign == '-') {
				calcMultDiv(zeiger + 1);
			}
			if (sign == '*' || sign == '/') {
				if (sign == '*') {
					op1 = op1 * op2;
					operand.set(zeiger, op1);
					operand.remove(zeiger + 1);
					operator.remove(zeiger);

					//----> Manuell alle zellen überschreiben...
					System.out.println("Zeiger:" + zeiger + " Operator: " + operator.toString() + "Sign: " + sign);
					calcMultDiv(zeiger);
				}

				if (sign == '/') {
					op1 = op1 / op2;
					operand.set(zeiger, op1);
					operand.remove(zeiger + 1);
					operator.remove(zeiger);

					System.out.println("Zeiger:" + zeiger + " Operator: " + operator.toString() + "Sign: " + sign);
					calcMultDiv(zeiger);
				}
			}
		}
	}

	private void verarbeiten() {

		operator.clear();
		operand.clear();

		String wert = dsb.toString();

		String[] wertSp = wert.split("[^\\d\\.]");
		char[] tmp = wert.toCharArray();

		//-------------Listen Füllen
		boolean doppel = false;
		for (char c : tmp) {
			if (c == '+' || c == '-' || c == '*' || c == '/') {
				operator.add(c);
				doppel = true;
				continue;
			}
//                        if ((c == '+' || c == '-' || c == '*' || c == '/') && doppel==true) {
//                doppel = false;
//                continue;
//
//            }

		}
		for (String werte : wertSp) {
			double tf = Double.valueOf(werte);
			operand.add(tf);
		}

		//-------------bererchnung
		int index = 0;
		for (char f : operator) {
			System.out.println("ZArray [ " + index + " ] : " + f);
			index++;
		}
		index = 0;
		for (double f : operand) {
			System.out.println("WArray [ " + index + " ] : " + f);
			index++;
		}
		calcMultDiv(0);
	}

}
