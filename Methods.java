public class Methods {
	
	public boolean equals(String str1, String str2){
	   	char []ch1=str1.toCharArray();
	   	char []ch2=str2.toCharArray();
	   	int count=0;
	   	for(int i=0;i<str2.length();i++){
	   		if(ch1[i]==ch2[i]) {
	   			count++;
	   		}
	   		else {
	   			return false;
	   		}	
	   	}
	            if(count==str2.length()) {
	           	 return true;
	            }
	            else {
	           	 return false;
	            }
	    }
		public boolean myStartWith(String str1,String str2){
			boolean value;
			int i,j=0,count=0;
			int d1=str1.length();
		    char[] ch1 = new char[d1];
		    ch1=str1.toCharArray();
		    int d2=str2.length();
		    int k=0;
		    char[] ch2 = new char[d2];
		    ch2 = str2.toCharArray();
		    for(i=0;i<d2;i++) {
		    	for(j=i;j<=i;j++) {
		    		if(ch1[i]==ch2[j]) {
		    			count++;
		    		}
		    		else {
				    	value = false;
				    	return value;
				    }
		    	}
		    }
		    if(d2==count) {
		    	value = true;
		    }
		    else {
		    	value = false;
		    }
		    return value;		    
		}
		
		public float abs(float value) {
	       return (value <= 0.0F) ? 0.0F - value : value;
	   }
}
