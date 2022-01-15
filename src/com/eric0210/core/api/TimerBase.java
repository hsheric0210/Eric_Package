package com.eric0210.core.api;

import java.util.Timer;
import java.util.TimerTask;

import com.eric0210.core.EricPackage;

public abstract class TimerBase
{
	private Timer timer;
	private boolean running;
	int remainingCount;
	private int startCount;

	// <editor-fold desc="Abstract methods">
	public void onTimerStart()
	{
	}

	public void onTimerStop()
	{
	}

	public void onTimerEnd()
	{
	}

	public abstract void onTimerRunning(final int remainingCount);
	// </editor-fold>

	// <editor-fold desc="Getter/Setters">
	public int getRemainingCount()
	{
		return remainingCount;
	}

	public void setRemainingCount(final int remainingCount)
	{
		this.remainingCount = remainingCount;
	}

	public int getStartCount()
	{
		return startCount;
	}

	public boolean isRunning()
	{
		return running;
	}
	// </editor-fold>

	public void reset()
	{
		remainingCount = startCount;
	}

	public void StartTimer(final int count)
	{
		if (count < 0)
		{
			EricPackage.debug("Can't create the timer - count can't be negative");
			return;
		}

		timer = new Timer();
		timer.schedule(new Task(), 1000L, 1000L);
		EricPackage.debug("Timer created with count " + count);
		remainingCount = count;
		startCount = count;
		onTimerStart();
	}

	public void StopTimer()
	{
		if (timer != null)
		{
			EricPackage.debug("Timer force stop - timer cancelled");
			timer.cancel();
		}
		remainingCount = 0;
		running = false;
		onTimerStop();
	}

	void EndTimer()
	{
		if (timer != null)
			timer.cancel();
		remainingCount = 0;
		running = false;
		onTimerEnd();
	}

	class Task extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				if (!EricPackage.instance().isEnabled())
					StopTimer();

				if (remainingCount <= 0)
				{
					EricPackage.debug("Timer ended");
					EndTimer();
					return;
				}

				onTimerRunning(remainingCount);
				remainingCount -= 1;
				EricPackage.debug("Current timer count: " + getRemainingCount() + " , Max Count: " + getStartCount());
			}
			catch (final Throwable t)
			{
				EricPackage.logException("Can't handle timer task", t);
			}
		}
	}
}
