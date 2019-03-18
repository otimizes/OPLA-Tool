package arquitetura.touml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Types {

    /*
     * Primitive Representations
     */
    public static final Type BOOLEAN = new Type() {
        public String getName() {
            return "Boolean";
        }
    };
    public static final Type BYTE = new Type() {
        public String getName() {
            return "Byte";
        }
    };
    public static final Type CHAR = new Type() {
        public String getName() {
            return "Char";
        }
    };
    public static final Type SHORT = new Type() {
        public String getName() {
            return "Short";
        }
    };
    public static final Type STRING = new Type() {
        public String getName() {
            return "String";
        }
    };
    public static final Type INTEGER = new Type() {
        public String getName() {
            return "Integer";
        }
    };
    public static final Type DOUBLE = new Type() {
        public String getName() {
            return "Double";
        }
    };
    public static final Type REAL = new Type() {
        public String getName() {
            return "Real";
        }
    };
    public static final Type FLOAT = new Type() {
        public String getName() {
            return "float";
        }
    };
    public static final Type LONG = new Type() {
        public String getName() {
            return "Longer";
        }
    };

    /*
     * Custom Type
     */
    public static Type custom(final String customType) {
        return new Type() {
            public String getName() {
                return customType;
            }
        };
    }

//	/*
//	 * Wrapper Representations
//	 */
//	public static final Type BOOLEAN_WRAPPER = new Type() {
//		public String getName() {
//			return "Boolean";
//		}
//	};
//	public static final Type BYTE_WRAPPER = new Type() {
//		public String getName() {
//			return "java.lang.Byte";
//		}
//	};
//	public static final Type CHAR_WRAPPER = new Type() {
//		public String getName() {
//			return "java.lang.Character";
//		}
//	};
//	public static final Type SHORT_WRAPPER = new Type() {
//		public String getName() {
//			return "java.lang.Short";
//		}
//	};
//	public static final Type INTEGER_WRAPPER = new Type() {
//		public String getName() {
//			return "Integer";
//		}
//	};
//	public static final Type DOUBLE_WRAPPER = new Type() {
//		public String getName() {
//			return "Double";
//		}
//	};
//	public static final Type FLOAT_WRAPPER = new Type() {
//		public String getName() {
//			return "Float";
//		}
//	};
//	public static final Type LONG_WRAPPER = new Type() {
//		public String getName() {
//			return "java.lang.Long";
//		}
//	};
// 
//	/*
//	 * String
//	 */
//	public static final Type STRING = new Type() {
//		public String getName() {
//			return "String";
//		}
//	};

    public static boolean isCustomType(String userType) {
        boolean custom = true;
        for (Field type : getNativeTypes()) {
            try {
                if (type.getName().equalsIgnoreCase(userType)) {
                    custom = false;
                    break;
                }
            } catch (Exception e) { /* who cares?? */ }
        }
        return custom;
    }

    private static List<Field> getNativeTypes() {
        List<Field> staticFields = new ArrayList<Field>();
        for (Field field : Types.class.getDeclaredFields())
            if (Modifier.isStatic(field.getModifiers()))
                staticFields.add(field);
        return staticFields;
    }

    public static Type getByName(final String type) {
        return new Type() {
            public String getName() {
                return type;
            }
        };
    }

    public interface Type {
        String getName();
    }
}