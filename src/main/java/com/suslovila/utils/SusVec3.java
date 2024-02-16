package com.suslovila.utils;


import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SusVec3 {
   //JUST COPIED FROM 1.18 VERSION BECAUSE I FIND VECTORS ON IT MORE COMFORTABLE
   public double x;
   public double y;
   public double z;
   public static final SusVec3 ZERO = new SusVec3(0.0D, 0.0D, 0.0D);

   public static SusVec3 fromRGB24(int p_82502_) {
      double d0 = (double)(p_82502_ >> 16 & 255) / 255.0D;
      double d1 = (double)(p_82502_ >> 8 & 255) / 255.0D;
      double d2 = (double)(p_82502_ & 255) / 255.0D;
      return new SusVec3(d0, d1, d2);
   }
   public SusVec3(Number x, Number y, Number z) {
      this.x = x.doubleValue();
      this.y = y.doubleValue();
      this.z = z.doubleValue();
   }

   public SusVec3(double p_82484_, double p_82485_, double p_82486_) {
      this.x = p_82484_;
      this.y = p_82485_;
      this.z = p_82486_;
   }
   public SusVec3(float p_82484_, float p_82485_, float p_82486_) {
      this.x = p_82484_;
      this.y = p_82485_;
      this.z = p_82486_;
   }
   public SusVec3(int p_82484_, int p_82485_, int p_82486_) {
      this.x = p_82484_;
      this.y = p_82485_;
      this.z = p_82486_;
   }

   public SusVec3 vectorTo(SusVec3 p_82506_) {
      return new SusVec3(p_82506_.x - this.x, p_82506_.y - this.y, p_82506_.z - this.z);
   }

   public SusVec3 normalize() {
      double d0 = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      return d0 < 1.0E-4D ? ZERO : new SusVec3(this.x / d0, this.y / d0, this.z / d0);
   }

   public double dot(SusVec3 p_82527_) {
      return this.x * p_82527_.x + this.y * p_82527_.y + this.z * p_82527_.z;
   }

   public SusVec3 cross(SusVec3 p_82538_) {
      return new SusVec3(this.y * p_82538_.z - this.z * p_82538_.y, this.z * p_82538_.x - this.x * p_82538_.z, this.x * p_82538_.y - this.y * p_82538_.x);
   }

   public SusVec3 subtract(SusVec3 p_82547_) {
      return this.subtract(p_82547_.x, p_82547_.y, p_82547_.z);
   }

   public SusVec3 subtract(double p_82493_, double p_82494_, double p_82495_) {
      return this.add(-p_82493_, -p_82494_, -p_82495_);
   }
   public SusVec3 subtract(int p_82493_, int p_82494_, int p_82495_) {
      return this.add(-p_82493_, -p_82494_, -p_82495_);
   }
   public SusVec3 add(SusVec3 p_82550_) {
      return this.add(p_82550_.x, p_82550_.y, p_82550_.z);
   }

   public SusVec3 add(double p_82521_, double p_82522_, double p_82523_) {
      return new SusVec3(this.x + p_82521_, this.y + p_82522_, this.z + p_82523_);
   }



   public double distanceTo(SusVec3 p_82555_) {
      double d0 = p_82555_.x - this.x;
      double d1 = p_82555_.y - this.y;
      double d2 = p_82555_.z - this.z;
      return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public double distanceToSqr(SusVec3 p_82558_) {
      double d0 = p_82558_.x - this.x;
      double d1 = p_82558_.y - this.y;
      double d2 = p_82558_.z - this.z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public double distanceToSqr(double p_82532_, double p_82533_, double p_82534_) {
      double d0 = p_82532_ - this.x;
      double d1 = p_82533_ - this.y;
      double d2 = p_82534_ - this.z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public SusVec3 scale(double p_82491_) {
      return this.multiply(p_82491_, p_82491_, p_82491_);
   }

   public SusVec3 reverse() {
      return this.scale(-1.0D);
   }

   public SusVec3 multiply(SusVec3 p_82560_) {
      return this.multiply(p_82560_.x, p_82560_.y, p_82560_.z);
   }

   public SusVec3 multiply(double p_82543_, double p_82544_, double p_82545_) {
      return new SusVec3(this.x * p_82543_, this.y * p_82544_, this.z * p_82545_);
   }

   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double lengthSqr() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public double horizontalDistance() {
      return Math.sqrt(this.x * this.x + this.z * this.z);
   }

   public double horizontalDistanceSqr() {
      return this.x * this.x + this.z * this.z;
   }

   public boolean equals(Object p_82552_) {
      if (this == p_82552_) {
         return true;
      } else if (!(p_82552_ instanceof SusVec3)) {
         return false;
      } else {
         SusVec3 SUSVec3 = (SusVec3)p_82552_;
         if (Double.compare(SUSVec3.x, this.x) != 0) {
            return false;
         } else if (Double.compare(SUSVec3.y, this.y) != 0) {
            return false;
         } else {
            return Double.compare(SUSVec3.z, this.z) == 0;
         }
      }
   }

   public int hashCode() {
      long j = Double.doubleToLongBits(this.x);
      int i = (int)(j ^ j >>> 32);
      j = Double.doubleToLongBits(this.y);
      i = 31 * i + (int)(j ^ j >>> 32);
      j = Double.doubleToLongBits(this.z);
      return 31 * i + (int)(j ^ j >>> 32);
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public SusVec3 lerp(SusVec3 p_165922_, double p_165923_) {
      return new SusVec3(lerp(p_165923_, this.x, p_165922_.x), lerp(p_165923_, this.y, p_165922_.y), lerp(p_165923_, this.z, p_165922_.z));
   }
   public static double lerp(double p_14140_, double p_14141_, double p_14142_) {
      return p_14141_ + p_14140_ * (p_14142_ - p_14141_);
   }
   public SusVec3 xRot(float p_82497_) {
      double f = Math.cos(p_82497_);
      double f1 = Math.sin(p_82497_);
      double d0 = this.x;
      double d1 = this.y * (double)f + this.z * (double)f1;
      double d2 = this.z * (double)f - this.y * (double)f1;
      return new SusVec3(d0, d1, d2);
   }

   public SusVec3 yRot(float p_82525_) {
      double f = Math.cos(p_82525_);
      double f1 = Math.sin(p_82525_);
      double d0 = this.x * (double)f + this.z * (double)f1;
      double d1 = this.y;
      double d2 = this.z * (double)f - this.x * (double)f1;
      return new SusVec3(d0, d1, d2);
   }

   public SusVec3 zRot(float p_82536_) {
      float f = (float)Math.cos(p_82536_);
      float f1 = (float)Math.sin(p_82536_);
      double d0 = this.x * (double)f + this.y * (double)f1;
      double d1 = this.y * (double)f - this.x * (double)f1;
      double d2 = this.z;
      return new SusVec3(d0, d1, d2);
   }


   public static SusVec3 directionFromRotation(float p_82499_, float p_82500_) {
      double f = Math.cos(-p_82500_ * ((float)Math.PI / 180F) - (float)Math.PI);
      double f1 = Math.sin(-p_82500_ * ((float)Math.PI / 180F) - (float)Math.PI);
      double f2 = -Math.cos(-p_82499_ * ((float)Math.PI / 180F));
      double f3 = Math.sin(-p_82499_ * ((float)Math.PI / 180F));
      return new SusVec3((double)(f1 * f2), (double)f3, (double)(f * f2));
   }


   public final double x() {
      return this.x;
   }

   public final double y() {
      return this.y;
   }

   public final double z() {
      return this.z;
   }

   public List<Double> getCordsAsList() {
      return  new ArrayList<>(Arrays.asList(this.x, this.y, this.z));

   }
   public static SusVec3 getVectorFromArrayList(ArrayList<Double> arrayList) throws Exception {
      if(arrayList.size() != 3) throw new Exception("Can not create Vector from array because length is not 3");
      return new SusVec3(arrayList.get(0), arrayList.get(1), arrayList.get(2));
   }
   public static SusVec3 getVec3FromForgeDirection(ForgeDirection direction) {
      return new SusVec3(direction.offsetX, direction.offsetY, direction.offsetZ);
   }
   public static double angleBetweenVec3(SusVec3 vec1, SusVec3 vec2){
      return Math.acos(vec1.dot(vec2)/vec1.length()/vec2.length());
   }

   public static<T extends Number> SusVec3 fromCollection(Collection<T> collection) throws Exception {
      if(collection.size() != 3) throw new Exception("collection size should be 3");
      Number[] array = new Number[3];
      collection.toArray(array);
      return new SusVec3(array[0], array[1], array[2]);
   }

   @NotNull
   public Integer component1() {
      return (int)x;
   }
   @NotNull
   public Integer component2() {
      return (int)y;
   }
   @NotNull
   public Integer component3() {
      return (int)z;
   }


}