package app;
import java.awt.event.MouseMotionAdapter;


public class MouseMoveApp extends MouseMotionAdapter {
	Mediator med;

	public MouseMoveApp(Mediator md) {
		super();
		med = md;
	}
}