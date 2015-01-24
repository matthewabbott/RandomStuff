package lab10;

import java.awt.Color;
import java.awt.event.KeyEvent;

import sedgewick.StdDraw;
import lab10.ArcadeKeys;
import lab10.FroggerGame;

public class ControlDemo implements FroggerGame {

	@Override
	public void playGame() {
		StdDraw.setScale(0, 1);

		while (!StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {

			drawControls(.25, .75, 0, Color.red);
			drawControls(.75, .75, 1, Color.blue);
			drawControls(.25, .25, 2, Color.green);
			drawControls(.75, .25, 3, Color.orange);

			StdDraw.show(100);

		}
	}

	private static final double BW = 0.025, BO = .05, JO = .2;

	private void drawControls(double cx, double cy, int playerNum, Color color) {
		StdDraw.setPenColor(color);

		drawButton(playerNum, ArcadeKeys.KEY_WHITE, cx, cy + .2, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_A, cx - BW * 2, cy - BO * 1.5, BW,
				color);
		drawButton(playerNum, ArcadeKeys.KEY_B, cx + BW, cy - BO, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_C, cx + BW * 4, cy - BO, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_D, cx + BW * 7, cy - BO, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_E, cx + BW, cy + BO, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_F, cx + BW * 4, cy + BO, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_G, cx + BW * 7, cy + BO, BW, color);

		double BW5 = BW * 1.5;
		drawButton(playerNum, ArcadeKeys.KEY_LEFT, cx - JO - BW5, cy, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_RIGHT, cx - JO + BW5, cy, BW,
				color);
		drawButton(playerNum, ArcadeKeys.KEY_UP, cx - JO, cy + BW5, BW, color);
		drawButton(playerNum, ArcadeKeys.KEY_DOWN, cx - JO, cy - BW5, BW, color);
	}

	private void drawButton(int playerNum, int button, double x, double y,
			double r, Color color) {
		if (ArcadeKeys.isKeyPressed(playerNum, button)) {
			StdDraw.setPenColor(color);
		} else {
			StdDraw.setPenColor(Color.white);
		}
		StdDraw.filledCircle(x, y, r);
		StdDraw.setPenColor(Color.black);
		StdDraw.circle(x, y, r);

	}

	@Override
	public String getGameName() {
		return "Control Demo";
	}

	@Override
	public String[] getTeamMembers() {
		String[] members = { "David Lu", "Doug Shook" };
		return members;
	}

	public static void main(String[] args){
		ControlDemo d = new ControlDemo();
		d.playGame();
	}
}
