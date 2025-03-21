use std::cell::RefCell;
use std::cell::Cell;


thread_local! {
    pub static LOGLEVEL: Cell<u8>  = Cell::new(2);
}
thread_local! {
    pub static INDENT: RefCell<String> = RefCell::new(String::from(""));
}

pub fn set_log_level(level: u8) {
    LOGLEVEL.set(level);
}
pub fn log_level() -> u8 {
    LOGLEVEL.get()
}

pub fn indent() {
    INDENT.with_borrow_mut(|s| {
        s.push_str("  ");
    })
}

pub fn dedent() {
    INDENT.with_borrow_mut(|s| { s.pop(); s.pop() } );
}


#[macro_export]
macro_rules! info {
    ($($fmt:expr),+) => {
        if $crate::log::LOGLEVEL.get() >= 3 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[34;1m\u{001b}[1mINFO\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($($fmt),+), file!(), line!())
            })
        }
    };
}

#[macro_export]
macro_rules! debg {
    ($($fmt:expr),+) => {
        if $crate::log::LOGLEVEL.get() >= 4 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[32mDEBG\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($($fmt),+), file!(), line!())
            })
        }
    };
}

#[macro_export]
macro_rules! debg2 {
    ($($fmt:expr),+) => {
        if $crate::log::LOGLEVEL.get() >= 5 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[32mDEBG\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($($fmt),+), file!(), line!())
            })
        }
    };
}

#[macro_export]
macro_rules! crit {
    ($($fmt:expr),+) => {
        if $crate::log::LOGLEVEL.get() >= 1 {
        $crate::log::INDENT.with_borrow(|s| {
            eprintln!("{}\u{001b}[31;1m\u{001b}[1mCRIT\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($($fmt),+), file!(), line!())
        })
    }
    };
}

#[macro_export]
macro_rules! warn {
    ($($fmt:expr),+) => {
        if $crate::log::LOGLEVEL.get() >= 2 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[33;1m\u{001b}[1mWARN\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($($fmt),+), file!(), line!())
            })
        }
    };
}

#[macro_export]
macro_rules! infob {
    ($fmt:literal, $e:expr) => {
        if $crate::log::LOGLEVEL.get() >= 3 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[36m\u{001b}[1mINFO\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($fmt), file!(), line!())
            });
            $crate::log::indent();
            let _result_ = $e;
            $crate::log::dedent();
            _result_
        } else {
            $e
        }
    };
}

#[macro_export]
macro_rules! debgb {
    ($fmt:literal, $e:expr) => {
        if $crate::log::LOGLEVEL.get() >= 4 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[32mDEBG\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($fmt), file!(), line!())
            });
            $crate::log::indent();
            let _result_ = $e;
            $crate::log::dedent();
            _result_
        } else {
            $e
        }
    };
}

#[macro_export]
macro_rules! debgb2 {
    ($fmt:literal, $e:expr) => {
        if $crate::log::LOGLEVEL.get() >= 5 {
            $crate::log::INDENT.with_borrow(|s| {
                eprintln!("{}\u{001b}[32mDEBG\u{001b}[0m \u{001b}[36m{:?}\u{001b}[0m {}:{}", s, format_args!($fmt), file!(), line!())
            });
            $crate::log::indent();
            let _result_ = $e;
            $crate::log::dedent();
            _result_
        } else {
            $e
        }
    };
}
