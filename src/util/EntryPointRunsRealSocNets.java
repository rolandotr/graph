package util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import attacks.AttackThreeMethod;

public abstract class EntryPointRunsRealSocNets {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		AttackThreeMethod.oneRunFacebookPanzURV(args);
	}

}
