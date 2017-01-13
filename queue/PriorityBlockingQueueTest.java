package queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest {
	private static BlockingQueue<MailEntity>	mailQueue	= new PriorityBlockingQueue<>();
	private static Random						random		= new Random();

	public static void main(String[] args) {
		sendData();
		putData();
	}

	public static void putData() {
        Thread putThread = new Thread(){
            @Override
            public void run(){
                ExecutorService putPool = Executors.newFixedThreadPool(5);
                while (true) {
                    MailEntity mailEntity = new MailEntity();
                    mailEntity.setName(String.valueOf(random.nextInt(100)));
                    mailEntity.setDesc(String.valueOf(random.nextInt(20)));
                    putPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(
                                        Thread.currentThread().getName() + "  put data into queue :" + mailEntity.getName());
                                mailQueue.put(mailEntity);
                                Thread.sleep(random.nextInt(10) * 500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        };
        putThread.start();
	}

	public static void sendData() {
		Thread sendThread = new Thread() {
			@Override
			public void run() {
                ExecutorService sendPool = Executors.newFixedThreadPool(3);
				try {
					while (true) {
						MailEntity mailEntity = mailQueue.take();
						sendPool.execute(new Runnable() {
							@Override
							public void run() {
								System.out.println(Thread.currentThread().getName() + "  send data "
										+ mailEntity.getName());
							}
						});
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
        sendThread.start();
	}

	public static BlockingQueue<MailEntity> getMailQueue() {
		return mailQueue;
	}

	public static void setMailQueue(BlockingQueue<MailEntity> mailQueue) {
		PriorityBlockingQueueTest.mailQueue = mailQueue;
	}
}
