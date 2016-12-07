
package com.techjar.vivecraftforge.util;

import net.minecraft.util.Vec3;

/**
 *
 * @author Techjar
 */
public class ServerQuaternion {
	public float w;
	public float x;
	public float y;
	public float z;

	public ServerQuaternion() {
		this.w = 1;
	}

	public ServerQuaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ServerQuaternion(ServerQuaternion other) {
		this.w = other.w;
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public ServerQuaternion copy() {
		return new ServerQuaternion(this);
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void normalize() {
		float norm = (float)Math.sqrt(w * w + x * x + y * y + z * z);
		if (norm > 0) {
			w /= norm;
			x /= norm;
			y /= norm;
			z /= norm;
		} else {
			w = 1;
			x = 0;
			y = 0;
			z = 0;
		}
	}

	public ServerQuaternion normalized() {
		float newW, newX, newY, newZ;
		float norm = (float)Math.sqrt(w * w + x * x + y * y + z * z);
		if (norm > 0) {
			newW = w / norm;
			newX = x / norm;
			newY = y / norm;
			newZ = z / norm;
		} else {
			newW = 1;
			newX = 0;
			newY = 0;
			newZ = 0;
		}
		return new ServerQuaternion(newW, newX, newY, newZ);
	}

	public ServerQuaternion multiply(ServerQuaternion other) {
		float newW = w * other.w - x * other.x - y * other.y - z * other.z;
		float newX = w * other.x + other.w * x + y * other.z - z * other.y;
		float newY = w * other.y + other.w * y - x * other.z + z * other.x;
		float newZ = w * other.z + other.w * z + x * other.y - y * other.x;
		return new ServerQuaternion(newW, newX, newY, newZ);
	}

	public Vec3 multiply(Vec3 vec) {
		float num = this.x * 2f;
		float num2 = this.y * 2f;
		float num3 = this.z * 2f;
		float num4 = this.x * num;
		float num5 = this.y * num2;
		float num6 = this.z * num3;
		float num7 = this.x * num2;
		float num8 = this.x * num3;
		float num9 = this.y * num3;
		float num10 = this.w * num;
		float num11 = this.w * num2;
		float num12 = this.w * num3;
		Vec3 result = Vec3.createVectorHelper(0, 0, 0);
		result.xCoord = (1f - (num5 + num6)) * vec.xCoord + (num7 - num12) * vec.yCoord + (num8 + num11) * vec.zCoord;
		result.yCoord = (num7 + num12) * vec.xCoord + (1f - (num4 + num6)) * vec.yCoord + (num9 - num10) * vec.zCoord;
		result.zCoord = (num8 - num11) * vec.xCoord + (num9 + num10) * vec.yCoord + (1f - (num4 + num5)) * vec.zCoord;
		return result;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + Float.floatToIntBits(this.w);
		hash = 23 * hash + Float.floatToIntBits(this.x);
		hash = 23 * hash + Float.floatToIntBits(this.y);
		hash = 23 * hash + Float.floatToIntBits(this.z);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ServerQuaternion other = (ServerQuaternion)obj;
		if (Float.floatToIntBits(this.w) != Float.floatToIntBits(other.w)) {
			return false;
		}
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Quaternion{" + "w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + '}';
	}
}
