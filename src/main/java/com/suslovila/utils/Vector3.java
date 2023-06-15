/*     */
package com.suslovila.utils;
/*     */ 
/*     */ import java.util.Formatter;
/*     */ import java.util.Locale;
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.ChunkCoordinates;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ public class Vector3
/*     */ {
/*  12 */   private static final Random RAND = new Random();
/*     */   
/*  14 */   public static final Vector3 ZERO = new Vector3(0, 0, 0);
/*     */   
/*     */   protected double x;
/*     */   protected double y;
/*     */   protected double z;
/*     */
/*     */   public Vector3() {
/*  21 */     this.x = 0.0D;
/*  22 */     this.y = 0.0D;
/*  23 */     this.z = 0.0D;
/*     */   }
/*     */   
/*     */   public Vector3(int x, int y, int z) {
/*  27 */     this.x = x;
/*  28 */     this.y = y;
/*  29 */     this.z = z;
/*     */   }
/*     */   
/*     */   public Vector3(double x, double y, double z) {
/*  33 */     this.x = x;
/*  34 */     this.y = y;
/*  35 */     this.z = z;
/*     */   }
/*     */   
/*     */   public Vector3(float x, float y, float z) {
/*  39 */     this.x = x;
/*  40 */     this.y = y;
/*  41 */     this.z = z;
/*     */   }
/*     */   
/*     */   public Vector3(Vec3 vec3) {
/*  45 */     this.x = vec3.xCoord;
/*  46 */     this.y = vec3.yCoord;
/*  47 */     this.z = vec3.zCoord;
/*     */   }
/*     */   
/*     */   public Vector3 add(Vector3 vec) {
/*  51 */     this.x += vec.x;
/*  52 */     this.y += vec.y;
/*  53 */     this.z += vec.z;
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 subtract(Vector3 vec) {
/*  58 */     this.x -= vec.x;
/*  59 */     this.y -= vec.y;
/*  60 */     this.z -= vec.z;
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 multiply(Vector3 vec) {
/*  65 */     this.x *= vec.x;
/*  66 */     this.y *= vec.y;
/*  67 */     this.z *= vec.z;
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 divide(Vector3 vec) {
/*  72 */     this.x /= vec.x;
/*  73 */     this.y /= vec.y;
/*  74 */     this.z /= vec.z;
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 divide(double divisor) {
/*  79 */     this.x /= divisor;
/*  80 */     this.y /= divisor;
/*  81 */     this.z /= divisor;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 copy(Vector3 vec) {
/*  86 */     this.x = vec.x;
/*  87 */     this.y = vec.y;
/*  88 */     this.z = vec.z;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public double length() {
/*  93 */     return Math.sqrt(lengthSquared());
/*     */   }
/*     */   
/*     */   public double lengthSquared() {
/*  97 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*     */   }
/*     */   
/*     */   public double distance(Vector3 o) {
/* 101 */     return Math.sqrt(distanceSquared(o));
/*     */   }
/*     */   
/*     */   public double distanceSquared(Vector3 o) {
/* 105 */     double difX = this.x - o.x;
/* 106 */     double difY = this.y - o.y;
/* 107 */     double difZ = this.z - o.z;
/* 108 */     return difX * difX + difY * difY + difZ * difZ;
/*     */   }
/*     */   
/*     */   public float angle(Vector3 other) {
/* 112 */     double dot = dot(other) / (length() * other.length());
/*     */     
/* 114 */     return (float)Math.acos(dot);
/*     */   }
/*     */   
/*     */   public Vector3 midpoint(Vector3 other) {
/* 118 */     this.x = (this.x + other.x) / 2.0D;
/* 119 */     this.y = (this.y + other.y) / 2.0D;
/* 120 */     this.z = (this.z + other.z) / 2.0D;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 getMidpoint(Vector3 other) {
/* 125 */     double x = (this.x + other.x) / 2.0D;
/* 126 */     double y = (this.y + other.y) / 2.0D;
/* 127 */     double z = (this.z + other.z) / 2.0D;
/* 128 */     return new Vector3(x, y, z);
/*     */   }
/*     */   
/*     */   public Vector3 multiply(int m) {
/* 132 */     this.x *= m;
/* 133 */     this.y *= m;
/* 134 */     this.z *= m;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 multiply(double m) {
/* 139 */     this.x *= m;
/* 140 */     this.y *= m;
/* 141 */     this.z *= m;
/* 142 */     return this;
/*     */   }
    //equals to multiply
/*     */   public Vector3 scale(double m) {
    /* 139 */     this.x *= m;
    /* 140 */     this.y *= m;
    /* 141 */     this.z *= m;
    /* 142 */     return new Vector3(this.x *= m, this.y *= m, this.z *= m);
    /*     */   }
/*     */   public Vector3 multiply(float m) {
/* 146 */     this.x *= m;
/* 147 */     this.y *= m;
/* 148 */     this.z *= m;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public double dot(Vector3 other) {
/* 153 */     return this.x * other.x + this.y * other.y + this.z * other.z;
/*     */   }
/*     */   
/*     */   public Vector3 crossProduct(Vector3 o) {
/* 157 */     double newX = this.y * o.z - o.y * this.z;
/* 158 */     double newY = this.z * o.x - o.z * this.x;
/* 159 */     double newZ = this.x * o.y - o.x * this.y;
/*     */     
///* 161 */     this.x = newX;
///* 162 */     this.y = newY;
///* 163 */     this.z = newZ;
/* 164 */     return new Vector3(newX, newY, newZ);
/*     */   }
/*     */   
/*     */   public Vector3 perpendicular() {
/* 168 */     if (this.z == 0.0D) {
/* 169 */       return zCrossProduct();
/*     */     }
/* 171 */     return xCrossProduct();
/*     */   }
/*     */   
/*     */   public Vector3 xCrossProduct() {
/* 175 */     double d = this.z;
/* 176 */     double d1 = -this.y;
/* 177 */     this.x = 0.0D;
/* 178 */     this.y = d;
/* 179 */     this.z = d1;
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 zCrossProduct() {
/* 184 */     double d = this.y;
/*     */     
/* 186 */     double d1 = -this.x;
/* 187 */     this.x = d;
/* 188 */     this.y = d1;
/* 189 */     this.z = 0.0D;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 yCrossProduct() {
/* 194 */     double d = -this.z;
/* 195 */     double d1 = this.x;
/* 196 */     this.x = d;
/* 197 */     this.y = 0.0D;
/* 198 */     this.z = d1;
/* 199 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 rotate(double angle, Vector3 axis) {
/* 203 */     Quat.aroundAxis(axis.clone().normalize(), angle).rotate(this);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 normalize() {
/* 208 */     double length = length();
/* 209 */     this.x /= length;
/* 210 */     this.y /= length;
/* 211 */     this.z /= length;
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 zero() {
/* 216 */     this.x = 0.0D;
/* 217 */     this.y = 0.0D;
/* 218 */     this.z = 0.0D;
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public static Vector3 random() {
/* 223 */     return new Vector3(RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1), RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1), RAND.nextDouble() * (RAND.nextBoolean() ? 1 : -1));
/*     */   }
/*     */   
/*     */   public static Vector3 positiveYRandom() {
/* 227 */     return random().setY(Math.abs(random().getY()));
/*     */   }
/*     */   
/*     */   public static Vector3 fromCC(ChunkCoordinates cc) {
/* 231 */     return new Vector3(cc.posX, cc.posY, cc.posZ);
/*     */   }
/*     */   
/*     */   public ChunkCoordinates getAsFloatCC() {
/* 235 */     return new ChunkCoordinates(Float.floatToIntBits((float)this.x), Float.floatToIntBits((float)this.y), Float.floatToIntBits((float)this.z));
/*     */   }
/*     */   
/*     */   public static Vector3 getFromFloatCC(ChunkCoordinates cc) {
/* 239 */     return new Vector3(Float.intBitsToFloat(cc.posX), Float.intBitsToFloat(cc.posY), Float.intBitsToFloat(cc.posZ));
/*     */   }
/*     */   
/*     */   public boolean isInAABB(Vector3 min, Vector3 max) {
/* 243 */     return (this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z);
/*     */   }
/*     */   
/*     */   public boolean isInSphere(Vector3 origin, double radius) {
/* 247 */     double difX = origin.x - this.x;
/* 248 */     double difY = origin.y - this.y;
/* 249 */     double difZ = origin.z - this.z;
/* 250 */     return (difX * difX + difY * difY + difZ * difZ <= radius * radius);
/*     */   }
/*     */   
/*     */   public Vector3 vectorFromHereTo(Vector3 target) {
/* 254 */     return new Vector3(target.x - this.x, target.y - this.y, target.z - this.z);
/*     */   }
/*     */   
/*     */   public double getX() {
/* 258 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getBlockX() {
/* 262 */     return (int)Math.floor(this.x);
/*     */   }
/*     */   
/*     */   public double getY() {
/* 266 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getBlockY() {
/* 270 */     return (int)Math.floor(this.y);
/*     */   }
/*     */   
/*     */   public double getZ() {
/* 274 */     return this.z;
/*     */   }
/*     */   
/*     */   public int getBlockZ() {
/* 278 */     return (int)Math.floor(this.z);
/*     */   }
/*     */   
/*     */   public Vector3 setX(int x) {
/* 282 */     this.x = x;
/* 283 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setX(double x) {
/* 287 */     this.x = x;
/* 288 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setX(float x) {
/* 292 */     this.x = x;
/* 293 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setY(int y) {
/* 297 */     this.y = y;
/* 298 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setY(double y) {
/* 302 */     this.y = y;
/* 303 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setY(float y) {
/* 307 */     this.y = y;
/* 308 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setZ(int z) {
/* 312 */     this.z = z;
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setZ(double z) {
/* 317 */     this.z = z;
/* 318 */     return this;
/*     */   }
/*     */   
/*     */   public Vector3 setZ(float z) {
/* 322 */     this.z = z;
/* 323 */     return this;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 327 */     if (!(obj instanceof Vector3)) {
/* 328 */       return false;
/*     */     }
/* 330 */     Vector3 other = (Vector3)obj;
/*     */     
/* 332 */     return (Math.abs(this.x - other.x) < 1.0E-6D && Math.abs(this.y - other.y) < 1.0E-6D && Math.abs(this.z - other.z) < 1.0E-6D && getClass().equals(obj.getClass()));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 336 */     int hash = 7;
/*     */     
/* 338 */     hash = 79 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32L);
/* 339 */     hash = 79 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32L);
/* 340 */     hash = 79 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32L);
/* 341 */     return hash;
/*     */   }
/*     */   
/*     */   public Vector3 clone() {
/* 345 */     return new Vector3(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 349 */     return this.x + "," + this.y + "," + this.z;
/*     */   }
/*     */   
/*     */   public static class Quat
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double z;
/*     */     public double s;
/*     */     
/*     */     public Quat() {
/* 360 */       this.s = 1.0D;
/* 361 */       this.x = 0.0D;
/* 362 */       this.y = 0.0D;
/* 363 */       this.z = 0.0D;
/*     */     }
/*     */     
/*     */     public Quat(Quat quat) {
/* 367 */       this.x = quat.x;
/* 368 */       this.y = quat.y;
/* 369 */       this.z = quat.z;
/* 370 */       this.s = quat.s;
/*     */     }
/*     */     
/*     */     public Quat(double d, double d1, double d2, double d3) {
/* 374 */       this.x = d1;
/* 375 */       this.y = d2;
/* 376 */       this.z = d3;
/* 377 */       this.s = d;
/*     */     }
/*     */     
/*     */     public void set(Quat quat) {
/* 381 */       this.x = quat.x;
/* 382 */       this.y = quat.y;
/* 383 */       this.z = quat.z;
/* 384 */       this.s = quat.s;
/*     */     }
/*     */     
/*     */     public static Quat aroundAxis(double ax, double ay, double az, double angle) {
/* 388 */       angle *= 0.5D;
/* 389 */       double d4 = Math.sin(angle);
/* 390 */       return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
/*     */     }
/*     */     
/*     */     public void multiply(Quat quat) {
/* 394 */       double d = this.s * quat.s - this.x * quat.x - this.y * quat.y - this.z * quat.z;
/* 395 */       double d1 = this.s * quat.x + this.x * quat.s - this.y * quat.z + this.z * quat.y;
/* 396 */       double d2 = this.s * quat.y + this.x * quat.z + this.y * quat.s - this.z * quat.x;
/* 397 */       double d3 = this.s * quat.z - this.x * quat.y + this.y * quat.x + this.z * quat.s;
/* 398 */       this.s = d;
/* 399 */       this.x = d1;
/* 400 */       this.y = d2;
/* 401 */       this.z = d3;
/*     */     }
/*     */     
/*     */     public void rightMultiply(Quat quat) {
/* 405 */       double d = this.s * quat.s - this.x * quat.x - this.y * quat.y - this.z * quat.z;
/* 406 */       double d1 = this.s * quat.x + this.x * quat.s + this.y * quat.z - this.z * quat.y;
/* 407 */       double d2 = this.s * quat.y - this.x * quat.z + this.y * quat.s + this.z * quat.x;
/* 408 */       double d3 = this.s * quat.z + this.x * quat.y - this.y * quat.x + this.z * quat.s;
/* 409 */       this.s = d;
/* 410 */       this.x = d1;
/* 411 */       this.y = d2;
/* 412 */       this.z = d3;
/*     */     }
/*     */     
/*     */     public double mag() {
/* 416 */       return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.s * this.s);
/*     */     }
/*     */     
/*     */     public void normalize() {
/* 420 */       double d = mag();
/* 421 */       if (d == 0.0D) {
/*     */         return;
/*     */       }
/* 424 */       d = 1.0D / d;
/* 425 */       this.x *= d;
/* 426 */       this.y *= d;
/* 427 */       this.z *= d;
/* 428 */       this.s *= d;
/*     */     }
/*     */     
/*     */     public void rotate(Vector3 vec) {
/* 432 */       double d = -this.x * vec.x - this.y * vec.y - this.z * vec.z;
/* 433 */       double d1 = this.s * vec.x + this.y * vec.z - this.z * vec.y;
/* 434 */       double d2 = this.s * vec.y - this.x * vec.z + this.z * vec.x;
/* 435 */       double d3 = this.s * vec.z + this.x * vec.y - this.y * vec.x;
/* 436 */       vec.x = d1 * this.s - d * this.x - d2 * this.z + d3 * this.y;
/* 437 */       vec.y = d2 * this.s - d * this.y + d1 * this.z - d3 * this.x;
/* 438 */       vec.z = d3 * this.s - d * this.z - d1 * this.y + d2 * this.x;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 442 */       StringBuilder stringbuilder = new StringBuilder();
/* 443 */       Formatter formatter = new Formatter(stringbuilder, Locale.US);
/* 444 */       formatter.format("Quaternion:\n", new Object[0]);
/* 445 */       formatter.format("  < %f %f %f %f >\n", new Object[] { Double.valueOf(this.s), Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z) });
/* 446 */       formatter.close();
/* 447 */       return stringbuilder.toString();
/*     */     }
/*     */     
/*     */     public static Quat aroundAxis(Vector3 axis, double angle) {
/* 451 */       return aroundAxis(axis.x, axis.y, axis.z, angle);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Илья\Downloads\ArcaneExpansion-1.3.2b-deobf.jar!\yellow\arcaneexpansion\commo\\utils\Vector3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */